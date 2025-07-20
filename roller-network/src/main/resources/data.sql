INSERT INTO roller (id, name, parent_id, referral_chain, exit) VALUES
  (0, 'CASINO', null, null, false);

INSERT INTO roller (name, parent_id, referral_chain, exit) VALUES
  ('PLAYER A', 0, '0', false),
  ('PLAYER B', 0, '0', false),
  ('PLAYER C', 0, '0', false),
  ('PLAYER D', 1, '1', false),
  ('PLAYER E', 1, '1', false),
  ('PLAYER F', 1, '1', false),
  ('PLAYER G', 1, '1', false),
  ('PLAYER H', 3, '3', false),
  ('PLAYER I', 3, '3', false),
  ('PLAYER M', 9, '9', false),
  ('PLAYER J', 3, '3', false),
  ('PLAYER K', 3, '3', false),
  ('PLAYER N', 12, '12', false);
