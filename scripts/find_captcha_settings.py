import pymysql

conn = pymysql.connect(host='127.0.0.1', port=3306, user='root', password='root')
cur = conn.cursor()
cur.execute('SHOW DATABASES')
for (db,) in cur.fetchall():
    if db in ('information_schema', 'mysql', 'performance_schema', 'sys'):
        continue
    try:
        cur.execute(f"USE `{db}`")
        cur.execute("SHOW TABLES LIKE 'Setting'")
        if cur.fetchone():
            cur.execute("SELECT Name, Value FROM Setting WHERE Name LIKE 'captchasettings%'")
            rows = cur.fetchall()
            if rows:
                print('DB:', db)
                for row in rows:
                    print(' ', row)
    except Exception as error:
        print('ERR', db, error)

conn.close()
