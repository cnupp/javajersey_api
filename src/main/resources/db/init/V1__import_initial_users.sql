INSERT INTO users(refId, name, role, real_name, primary_role, department) VALUES
  (11111, "admin", "ADMIN", 'admin', 'Admin', 'Operations'),
  (22222, "Wang", "PROJECT_MANAGER", 'Lao Wang', 'PM', 'Professional Services'),
  (33333, "Zhang", "PROJECT_MANAGER", 'Lao Zhang', 'PM', 'Professional Services'),
  (100, 'shanchuan', 'EMPLOYEE', 'Xu Shanchuan', 'Dev', 'Professional Services'),
  (101, 'xiaoming', 'EMPLOYEE', 'Wang Xiaoming', 'Dev', 'Professional Services');

INSERT INTO users(refId, name, role) VALUES(102, 'bg', 'BACKGROUND_JOB');

INSERT INTO solutions(id, name, description) values(1, 'data api', 'description for data api'), (2, 'single page app', 'description for single page app');

INSERT INTO stacks(id, name, description) values
  (1, 'java + jersey', 'jersey 2.0 and mysql with innodb and mybatis'),
  (2, 'rails + mongodb', 'stack of rails 4 and mongodb with mongoid as mapper');

INSERT INTO services (id, name, image_url) values
  (10, 'java', 'http://img.stackshare.io/service/995/K85ZWV2F.png'),
  (11, 'jersey', 'https://jersey.java.net/images/jersey_logo.png'),
  (12, 'mysql', 'http://img.stackshare.io/service/1025/logo-mysql-170x170.png'),
  (13, 'Ruby', 'http://img.stackshare.io/service/989/ruby.png'),
  (14, 'Mongodb', 'http://img.stackshare.io/service/1030/mX8rO3vw.png');


INSERT INTO stack_services (stack_id, service_id) values
  (1, 10), (1, 11), (1, 12),
  (2, 13), (2, 14);

INSERT INTO projects(project_id, name, account) values(100, 'ketsu', 'ThoughtWorks'), (101, 'p2p', 'KingSoft');

INSERT INTO assignments(user_id, project_id, starts_at, ends_at) values(22222, 100, '2015-12-12', '2016-12-12'), (22222, 101, '2015-12-12', '2016-12-12'), (100, 100, '2015-12-12', '2016-12-12'), (101, 100, '2015-12-12', '2016-12-12');

INSERT INTO capabilities(id, project_id, solution_id, stack_id) values
  (1, 100, 1, 1),
  (2, 100, 2, 2),
  (3, 100, 1, 2);

INSERT INTO qualifications(user_id, capability_id) values
  (22222, 1),
  (22222, 2),
  (22222, 3),
  (100, 1),
  (101, 2);

INSERT INTO evaluations(id, project_id, user_id, capability_id, commit_uri) values
            (1, 100, 22222, 1, 'http://github.com/code/1');
INSERT INTO evaluation_results(evaluation_id, project_id, user_id, status, score) values
            (1, 100, 22222, 'pass', 90);

