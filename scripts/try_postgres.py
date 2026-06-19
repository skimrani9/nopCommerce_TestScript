import psycopg2

CREDS = [
    ("postgres", "postgres"),
    ("postgres", "nopCommerce_db_password"),
    ("postgres", "root"),
    ("nopcommerce", "nopCommerce_db_password"),
]

for user, password in CREDS:
    try:
        conn = psycopg2.connect(
            host="127.0.0.1",
            port=5432,
            user=user,
            password=password,
            dbname="postgres",
            connect_timeout=3,
        )
        cur = conn.cursor()
        cur.execute("SELECT datname FROM pg_database")
        print("OK", user, password, [r[0] for r in cur.fetchall()])
        conn.close()
    except Exception as error:
        print("FAIL", user, password, str(error)[:120])
