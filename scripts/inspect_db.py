import pymysql

conn = pymysql.connect(host='127.0.0.1', port=3306, user='root', password='root', database='ecommerce_app_database')
cur = conn.cursor()
cur.execute('SHOW TABLES')
tables = [r[0] for r in cur.fetchall()]
print('Table count:', len(tables))
for t in tables[:30]:
    print(t)
cur.execute("SHOW TABLES LIKE '%setting%'")
print('Setting-like:', cur.fetchall())
conn.close()
