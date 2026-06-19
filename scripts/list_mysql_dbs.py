import pymysql

conn = pymysql.connect(host='127.0.0.1', port=3306, user='root', password='root')
cur = conn.cursor()
cur.execute('SHOW DATABASES')
for (db,) in cur.fetchall():
    if db in ('information_schema', 'mysql', 'performance_schema', 'sys'):
        continue
    cur.execute(f"USE `{db}`")
    cur.execute('SHOW TABLES')
    tables = [r[0] for r in cur.fetchall()]
    if not tables:
        continue
    print('DB', db, 'tables', len(tables), 'sample', tables[:5])

conn.close()
