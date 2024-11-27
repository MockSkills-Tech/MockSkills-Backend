# 📋 **Database Documentation: `mockskills`**

The **`mockskills`** database is designed for managing user registrations for the **CollabZone** platform. It includes essential details about users, such as contact information, professional skills, and social media profiles.

---

## 🔍 **1. Database Overview**

The `mockskills` database focuses on storing user profiles with structured information, including professional details, skills, and unique identifiers. This system supports collaboration and resource management for various departments.

---

## 📂 **2. Tables and Description**

### 📝 **2.1 Table: `CollabZone_JoinUs_Register`**

This table stores user information, including personal, professional, and social media data. Each user receives a **formatted ID** generated automatically via a trigger.

#### 📊 **Table Structure**

| 🏷️ **Column Name**      | 🔢 **Data Type**   | 🔐 **Constraints**                                                            | 📝 **Description**                                                                                                                                               |
|--------------------------|-------------------|-------------------------------------------------------------------------------|-------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| `id`                    | `BIGINT`         | `AUTO_INCREMENT`, `PRIMARY KEY`                                              | 🔑 Unique identifier for each user.                                                                                                                               |
| `formatted_id`          | `VARCHAR(10)`    | `NOT NULL`                                                                   | 📇 Auto-generated ID (e.g., GENZ00001).                                                                                                                          |
| `email`                 | `VARCHAR(255)`   | `NOT NULL`                                                                   | 📧 User's email address.                                                                                                                                          |
| `name`                  | `VARCHAR(255)`   | `NOT NULL`                                                                   | 🧑 User's full name.                                                                                                                                              |
| `location`              | `VARCHAR(255)`   | -                                                                             | 📍 User's geographic location.                                                                                                                                   |
| `bio`                   | `TEXT`           | -                                                                             | ✍️ User's biography or summary (optional).                                                                                                                        |
| `department`            | `ENUM`           | `NOT NULL`                                                                   | 🏢 Department the user belongs to (`TECH`, `FINANCE`, `MARKETING`, `CONSULTANCY`).                                                                                 |
| `profile_picture_url`   | `TEXT`           | -                                                                             | 🖼️ URL of the user's profile picture (optional).                                                                                                                   |
| `portfolio_url`         | `TEXT`           | -                                                                             | 🌐 Link to the user's portfolio (optional).                                                                                                                       |
| `github_url`            | `TEXT`           | -                                                                             | 🐱 Link to the user's GitHub profile (optional).                                                                                                                   |
| `linkedin_url`          | `TEXT`           | -                                                                             | 🔗 Link to the user's LinkedIn profile (optional).                                                                                                                 |
| `skills`                | `JSON`           | -                                                                             | 🛠️ User's skills, stored as JSON data (optional).                                                                                                                 |
| `join_date`             | `TIMESTAMP`      | `DEFAULT CURRENT_TIMESTAMP`                                                  | 📅 Auto-generated timestamp indicating when the user joined.                                                                                                       |

---

## ⚙️ **3. Triggers**

### 🚦 **Trigger: `before_insert_CollabZone_JoinUs_Register`**

#### 🎯 **Purpose**:
Automatically generates a unique `formatted_id` for every new user.

#### 🔧 **How It Works**:
1. Retrieves the current maximum `id` in the table.
2. Adds 1 to calculate the next unique ID.
3. Formats the ID with the prefix `GENZ` and pads it to ensure 5 digits.
4. Assigns the generated ID to the `formatted_id` field.

#### 🛠️ **Trigger Code**:
```sql
DELIMITER $$

CREATE TRIGGER before_insert_CollabZone_JoinUs_Register
BEFORE INSERT ON CollabZone_JoinUs_Register
FOR EACH ROW
BEGIN
    DECLARE next_id INT;
    SET next_id = (SELECT IFNULL(MAX(id), 0) + 1 FROM CollabZone_JoinUs_Register);
    SET NEW.formatted_id = CONCAT('GENZ', LPAD(next_id, 5, '0'));
END$$

DELIMITER ;
```

---

## 📋 **4. Sample Table Data**

| 🆔 **ID** | 📇 **Formatted ID** | 📧 **Email**             | 🧑 **Name**     | 📍 **Location**      | ✍️ **Bio**                                                                                                                         | 🏢 **Department** | 🖼️ **Profile Picture URL**          | 🌐 **Portfolio URL**             | 🐱 **GitHub URL**             | 🔗 **LinkedIn URL**               | 🛠️ **Skills**                                                              | 📅 **Join Date**        |
|-----------|----------------------|--------------------------|-----------------|----------------------|-------------------------------------------------------------------------------------------------------------------------------------|--------------------|---------------------------------------|-----------------------------------|---------------------------|------------------------------------|------------------------------------------------------------------------|----------------------|
| 1         | GENZ00001            | kundagetcode@gmail.com   | Kundan Singh    | Bihar, Patna, India | Finance enthusiast with expertise in portfolio management and equity analysis, focused on optimizing investment strategies...      | TECH               | https://example.com/profile1.jpg    | https://kundansings.netlify.app/  | https://github.com/kundansinghdev | https://www.linkedin.com/in/kundanks/ | ["Java", "Spring Boot", "React", "SQL", "JavaScript", "Microservices", "DSA", "CPP"] | 2024-11-20 16:41:55 |

---
