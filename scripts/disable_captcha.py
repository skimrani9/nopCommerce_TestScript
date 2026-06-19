import sys

import pymysql

CONNECTIONS = [
    {"host": "127.0.0.1", "port": 3306, "user": "root", "password": "root", "database": "ecommerce_app_database"},
    {"host": "127.0.0.1", "port": 3306, "user": "root", "password": "root", "database": "nopcommerce"},
    {"host": "127.0.0.1", "port": 3306, "user": "root", "password": "root", "database": "nopCommerce"},
]


def disable_captcha(connection_info):
    conn = pymysql.connect(connect_timeout=5, **connection_info)
    cur = conn.cursor()
    cur.execute("SHOW TABLES LIKE 'Setting'")
    if not cur.fetchone():
        conn.close()
        return False

    updates = [
        ("captchasettings.enabled", "False"),
        ("captchasettings.showonloginpage", "False"),
        ("captchasettings.recaptchapublickey", "6LeIxAcTAAAAAJcZVRqyHhHrmPgi4y0B6S_EPNdZ6"),
        ("captchasettings.recaptchaprivatekey", "6LeIxAcTAAAAAGG-vFI1TnRWxMZNQuJWeWf9A"),
    ]
    for name, value in updates:
        cur.execute("UPDATE Setting SET Value=%s WHERE Name=%s", (value, name))
        if cur.rowcount == 0:
            cur.execute("INSERT INTO Setting (Name, Value, StoreId) VALUES (%s, %s, 0)", (name, value))

    conn.commit()
    conn.close()
    return True


def main():
    for connection_info in CONNECTIONS:
        try:
            if disable_captcha(connection_info):
                print(f"Disabled CAPTCHA using database {connection_info['database']}")
                return 0
        except Exception as error:
            print(f"Failed for {connection_info['database']}: {error}", file=sys.stderr)
    return 1


if __name__ == "__main__":
    raise SystemExit(main())
