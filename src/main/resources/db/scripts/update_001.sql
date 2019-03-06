CREATE TABLE items (
  id          serial,
  description text,
  created     bigint,
  done        boolean
);

GRANT ALL PRIVILEGES ON TABLE items TO ${db.username};
GRANT USAGE, SELECT ON ALL SEQUENCES IN SCHEMA public TO ${db.username};