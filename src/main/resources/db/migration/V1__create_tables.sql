
create table projects (
  id integer PRIMARY KEY auto_increment,
  project_id integer NOT NULL,
  name varchar(255) NOT NULL,
  account VARCHAR(255) NOT NULL
);

create table assignments (
  id INTEGER PRIMARY KEY AUTO_INCREMENT,
  project_id INTEGER,
  deleted TINYINT,
  user_id INTEGER,
  starts_at VARCHAR(255) NOT NULL,
  ends_at VARCHAR(255) NOT NULL,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

create table capabilities (
  id INTEGER PRIMARY KEY AUTO_INCREMENT,
  project_id INTEGER,
  stack_id INTEGER,
  solution_id INTEGER,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

create TABLE stacks (
  id INTEGER PRIMARY KEY AUTO_INCREMENT,
  description TEXT,
  name VARCHAR(255)
);

create TABLE solutions (
  id INTEGER PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(255),
  description TEXT,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

create TABLE users (
  id INTEGER PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(255),
  real_name VARCHAR(255),
  primary_role VARCHAR(255),
  department VARCHAR(255),
  refId VARCHAR(255),
  role VARCHAR(255),
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  UNIQUE (refId)
);

create TABLE evaluations (
  id INTEGER PRIMARY KEY AUTO_INCREMENT,
  project_id INTEGER,
  user_id INTEGER,
  capability_id INTEGER,
  commit_uri VARCHAR(255),
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE evaluation_results (
  id INTEGER PRIMARY KEY AUTO_INCREMENT,
  project_id INTEGER,
  user_id INTEGER,
  evaluation_id INTEGER,
  score INTEGER NOT NULL,
  status VARCHAR(255) NOT NULL DEFAULT 'NEW',
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

create TABLE qualifications (
  id INTEGER PRIMARY KEY AUTO_INCREMENT,
  user_id INTEGER,
  capability_id INTEGER,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE services (
  id INTEGER PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(255),
  image_url VARCHAR(255)
);

CREATE TABLE stack_services (
  id INTEGER PRIMARY KEY AUTO_INCREMENT,
  stack_id INTEGER NOT NULL,
  service_id INTEGER NOT NULL
);

ALTER TABLE `stack_services` ADD UNIQUE `unique_index`(`stack_id`, `service_id`);
