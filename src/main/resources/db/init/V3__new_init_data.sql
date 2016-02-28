DELETE FROM users;
DELETE FROM solutions;
DELETE FROM stacks;
DELETE FROM services;
DELETE FROM stack_services;
DELETE FROM projects;
DELETE FROM assignments;
DELETE FROM capabilities;
DELETE FROM qualifications;
DELETE FROM evaluation_results;
DELETE FROM evaluations;

ALTER TABLE users AUTO_INCREMENT = 1;
ALTER TABLE solutions AUTO_INCREMENT = 1;
ALTER TABLE stacks AUTO_INCREMENT = 1;
ALTER TABLE services AUTO_INCREMENT = 1;
ALTER TABLE stack_services AUTO_INCREMENT = 1;
ALTER TABLE projects AUTO_INCREMENT = 1;
ALTER TABLE assignments AUTO_INCREMENT = 1;
ALTER TABLE capabilities AUTO_INCREMENT = 1;
ALTER TABLE qualifications AUTO_INCREMENT = 1;
ALTER TABLE evaluations AUTO_INCREMENT = 1;
ALTER TABLE evaluation_results AUTO_INCREMENT = 1;


INSERT INTO users(refId, name, role, real_name, primary_role, department) VALUES
  (11111, "admin", "ADMIN", 'admin', 'Admin', 'Operations'),
  (22222, "Wang", "PROJECT_MANAGER", 'Wang Xiaoming', 'PM', 'Professional Services'),
  (33333, "Zhang", "PROJECT_MANAGER", 'Zhang jiankang', 'PM', 'Professional Services'),
  (100, 'shanchuan', 'EMPLOYEE', 'Xu Shanchuan', 'Dev', 'Professional Services'),
  (101, 'xiaoming', 'EMPLOYEE', 'Xiaoming', 'Dev', 'Professional Services');

INSERT INTO users(refId, name, role) VALUES(102, 'bg', 'BACKGROUND_JOB');

INSERT INTO solutions(id, name, description) values(1, 'api', 'description for data api'), (2, 'spa', 'description for single page app');

INSERT INTO stacks(id, name, description) values
  (1, 'javajersey', 'jersey 2.0 and mysql with innodb and mybatis'),
  (2, 'react', 'stack of react with redux');

INSERT INTO services (id, name, image_url) values
  (10, 'Java', 'http://img.stackshare.io/service/995/K85ZWV2F.png'),
  (11, 'Jersey', 'https://jersey.java.net/images/jersey_logo.png'),
  (12, 'MySQL', 'http://img.stackshare.io/service/1025/logo-mysql-170x170.png'),
  (13, 'React', 'http://img.stackshare.io/service/1020/OYIaJ1KK.png'),
  (14, 'Webpack', 'http://img.stackshare.io/service/1682/2105791.png ');


INSERT INTO stack_services (stack_id, service_id) values
  (1, 10), (1, 11), (1, 12),
  (2, 13), (2, 14);

INSERT INTO projects(project_id, name, account) values(100, 'ketsu', 'ThoughtWorks');

INSERT INTO assignments(user_id, project_id, starts_at, ends_at) values(22222, 100, '2015-12-12', '2016-12-12'), (100, 100, '2015-12-12', '2016-12-12'), (101, 100, '2015-12-12', '2016-12-12');

INSERT INTO capabilities(id, project_id, solution_id, stack_id) values
  (1, 100, 1, 1),
  (2, 100, 2, 2);

INSERT INTO qualifications(user_id, capability_id) values
  (22222, 1),
  (22222, 2),
  (100, 1),
  (101, 2);

INSERT INTO evaluations(id, project_id, user_id, capability_id) values (1, 100, 22222, 1);
INSERT INTO evaluation_results(evaluation_id, project_id, user_id, status, score) values (1, 100, 22222, 'PASSED', 5600);

