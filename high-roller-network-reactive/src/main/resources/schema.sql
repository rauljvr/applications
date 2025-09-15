CREATE TABLE IF NOT EXISTS player (
  id INT NOT NULL AUTO_INCREMENT,
  name VARCHAR(20),
  parent_id INT,
  referral_chain VARCHAR(255),
  exit BOOLEAN,
  PRIMARY KEY (id)
);