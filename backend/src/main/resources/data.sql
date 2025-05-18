CREATE TABLE employee (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100),
    age INT,
    salary DECIMAL(10,2)
);

CREATE TABLE department (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100)
);

INSERT INTO employee (name, age, salary) VALUES ('Alice', 30, 5000.00);
INSERT INTO employee (name, age, salary) VALUES ('Bob', 40, 7000.00);
INSERT INTO employee (name, age, salary) VALUES ('Charlie', 25, 4000.00);

INSERT INTO department (name) VALUES ('HR');
INSERT INTO department (name) VALUES ('Engineering');
INSERT INTO department (name) VALUES ('Sales');

CREATE TABLE RT_Utility_MV (
    id INT PRIMARY KEY AUTO_INCREMENT,
    code VARCHAR(50),
    description VARCHAR(255)
);
INSERT INTO RT_Utility_MV (code, description) VALUES
('UTIL1', 'Utility 1'),
('UTIL2', 'Utility 2'),
('UTIL3', 'Utility 3'),
('UTIL4', 'Utility 4'),
('UTIL5', 'Utility 5');

CREATE TABLE RT_Asset_MV (
    id INT PRIMARY KEY AUTO_INCREMENT,
    code VARCHAR(50),
    description VARCHAR(255)
);
INSERT INTO RT_Asset_MV (code, description) VALUES
('ASSET1', 'Asset 1'),
('ASSET2', 'Asset 2'),
('ASSET3', 'Asset 3'),
('ASSET4', 'Asset 4'),
('ASSET5', 'Asset 5'),
('AS1', 'New Asset');

CREATE TABLE RT_Service_MV (
    id INT PRIMARY KEY AUTO_INCREMENT,
    code VARCHAR(50),
    description VARCHAR(255)
);
INSERT INTO RT_Service_MV (code, description) VALUES
('SERV1', 'Service 1'),
('SERV2', 'Service 2'),
('SERV3', 'Service 3'),
('SERV4', 'Service 4'),
('SERV5', 'Service 5');

CREATE TABLE RT_Config_MV (
    id INT PRIMARY KEY AUTO_INCREMENT,
    code VARCHAR(50),
    description VARCHAR(255)
);
INSERT INTO RT_Config_MV (code, description) VALUES
('CONF1', 'Config 1'),
('CONF2', 'Config 2'),
('CONF3', 'Config 3'),
('CONF4', 'Config 4'),
('CONF5', 'Config 5');

CREATE TABLE RT_All_MV (
    id INT PRIMARY KEY AUTO_INCREMENT,
    utility_id INT,
    asset_id INT,
    service_id INT,
    config_id INT,
    FOREIGN KEY (utility_id) REFERENCES RT_Utility_MV(id),
    FOREIGN KEY (asset_id) REFERENCES RT_Asset_MV(id),
    FOREIGN KEY (service_id) REFERENCES RT_Service_MV(id),
    FOREIGN KEY (config_id) REFERENCES RT_Config_MV(id)
);
INSERT INTO RT_All_MV (utility_id, asset_id, service_id, config_id) VALUES
(1, 1, 1, 1),
(2, 2, 2, 2),
(3, 3, 3, 3),
(4, 4, 4, 4),
(5, 5, 5, 5);
