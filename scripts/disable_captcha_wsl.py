import os
import subprocess
import sys
import time
import urllib.error
import urllib.request

BASE_URL = os.environ.get("NOP_BASE_URL", "http://localhost:5000").rstrip("/")

DISABLE_SQL = (
    "UPDATE \"Setting\" SET \"Value\"='False' "
    "WHERE \"Name\" IN ('captchasettings.enabled', 'captchasettings.showonloginpage'); "
    "UPDATE \"Setting\" SET \"Value\"='6LeIxAcTAAAAAJcZVRqyHhHrmPgi4y0B6S_EPNdZ6' "
    "WHERE \"Name\"='captchasettings.recaptchapublickey'; "
    "UPDATE \"Setting\" SET \"Value\"='6LeIxAcTAAAAAGG-vFI1TnRWxMZNQuJWeWf9A' "
    "WHERE \"Name\"='captchasettings.recaptchaprivatekey';"
)

CHECK_SQL = (
    "SELECT COUNT(*) FROM \"Setting\" "
    "WHERE \"Name\" IN ('captchasettings.enabled', 'captchasettings.showonloginpage') "
    "AND \"Value\"='False';"
)


def run_wsl(command: str) -> tuple[int, str, str]:
    completed = subprocess.run(
        ["wsl", "-e", "bash", "-lc", command],
        capture_output=True,
        text=True,
        timeout=120,
    )
    return completed.returncode, completed.stdout, completed.stderr


def wait_for_store(timeout_seconds: int = 90) -> bool:
    deadline = time.time() + timeout_seconds
    while time.time() < deadline:
        try:
            with urllib.request.urlopen(f"{BASE_URL}/", timeout=5) as response:
                if response.status == 200:
                    return True
        except (urllib.error.URLError, TimeoutError):
            time.sleep(2)
    return False


def captcha_already_disabled() -> bool:
    check_sql = CHECK_SQL.replace("'", "'\\''")
    command = (
        "docker exec nopcommerce_postgres_server "
        f"psql -U postgres -d nopcommerce -tAc '{check_sql}'"
    )
    code, stdout, _stderr = run_wsl(command)
    return code == 0 and stdout.strip() == "2"


IMPERSONATION_SQL = (
    "DELETE FROM \"GenericAttribute\" "
    "WHERE \"KeyGroup\"='Customer' AND \"Key\"='ImpersonatedCustomerId';"
)


def clear_stale_impersonation() -> int:
    sql_command = IMPERSONATION_SQL.replace("'", "'\\''")
    psql = (
        "docker exec nopcommerce_postgres_server "
        f"psql -U postgres -d nopcommerce -c '{sql_command}'"
    )
    code, stdout, stderr = run_wsl(psql)
    output = (stdout + stderr).strip()
    if output:
        print(output)
    return code


def main() -> int:
    impersonation_code = clear_stale_impersonation()
    if impersonation_code != 0:
        return impersonation_code

    if captcha_already_disabled():
        print("CAPTCHA already disabled")
        if wait_for_store(90):
            return 0
        print("Store not ready", file=sys.stderr)
        return 1

    sql_command = DISABLE_SQL.replace("'", "'\\''")
    psql = (
        "docker exec nopcommerce_postgres_server "
        f"psql -U postgres -d nopcommerce -c '{sql_command}'"
    )
    code, stdout, stderr = run_wsl(psql)
    output = (stdout + stderr).strip()
    if output:
        print(output)
    if code != 0:
        return code

    restart_code, restart_out, restart_err = run_wsl("docker restart nopcommerce")
    if restart_out.strip():
        print(restart_out.strip())
    if restart_err.strip():
        print(restart_err.strip(), file=sys.stderr)
    if restart_code != 0:
        return restart_code

    if wait_for_store():
        print("Store is ready")
        return 0

    print("Store did not become ready in time", file=sys.stderr)
    return 1


if __name__ == "__main__":
    raise SystemExit(main())
