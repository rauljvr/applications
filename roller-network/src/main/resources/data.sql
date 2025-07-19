INSERT INTO roller (id, name, parent_id, referral_id) VALUES
  (0, 'CASINO', null, null);

INSERT INTO roller (name, parent_id, referral_id) VALUES
  ('PLAYER A', 0, 0),
  ('PLAYER B', 0, 0),
  ('PLAYER C', 0, 0),
  ('PLAYER D', 1, 1),
  ('PLAYER E', 1, 1),
  ('PLAYER F', 1, 1),
  ('PLAYER G', 1, 1),
  ('PLAYER H', 3, 3),
  ('PLAYER I', 3, 3),
  ('PLAYER M', 9, 9),
  ('PLAYER J', 3, 3),
  ('PLAYER K', 3, 3),
  ('PLAYER N', 12, 12);
