INSERT INTO roller (id, name, parent_id, depth) VALUES
  (0, 'CASINO', null, 0);

INSERT INTO roller (name, parent_id, depth) VALUES
  ('PLAYER A', 0, 1),
  ('PLAYER B', 0, 1),
  ('PLAYER C', 0, 1),
  ('PLAYER D', 1, 2),
  ('PLAYER E', 1, 2),
  ('PLAYER F', 1, 2),
  ('PLAYER G', 1, 2),
  ('PLAYER H', 3, 2),
  ('PLAYER I', 3, 2),
  ('PLAYER M', 9, 3),
  ('PLAYER J', 3, 2),
  ('PLAYER K', 3, 2),
  ('PLAYER N', 12, 3);
