# Pool Scheduling System - Asgard Challenge Solution

This Android project is a complete, functional solution to the Asgard Labs developer challenge. The application algorithmically manages and generates a weekly swimming lesson schedule, taking into account a wide range of constraints related to students, instructors, and pool availability.

## Core Features

* **Student Management:** Add and remove students from a dynamic list, with a maximum of 30 students enforced in code.&#x20;
* **Student Preferences:** For each student, you can define:

  * Desired swimming style (Freestyle, Breaststroke, Butterfly, Backstroke)&#x20;
  * Preferred lesson type (Private only, Group only, or Either)&#x20;
* **Smart Scheduling Algorithm:** Generate a weekly schedule with a single button press, based on an algorithm that groups students by style and type preference, attempts to form group lessons first (minimum of 2, maximum of 6 per lesson), and then assigns remaining students to private lessons.
* **Conflict Management:** Displays a clear report detailing which students could not be scheduled and the reasons (no available instructor, time conflicts, or mismatched preferences).&#x20;
* **Schedule Display:** Presents the list of scheduled lessons clearly in a `ListView`, with each item showing day, time, lesson type, style, instructor, and students. Tapping on any lesson opens a dialog with full details.&#x20;
* **Data Persistence:** The application saves the student list and the last generated schedule to `SharedPreferences` (using Gson for JSON serialization), so data is retained between sessions.&#x20;

## Architecture and Technologies

This project was built using modern Android development practices, with an emphasis on clean, organized, and scalable code.

* **Language:** Java
* **Platform:** Native Android (Android Studio)
* **UI:** XML Layouts featuring `MaterialCardView` for a modern look.
* **Local Storage:** `SharedPreferences` combined with the `Gson` library for object serialization.
* **Design Patterns:**

  * **Adapter Pattern:** `StudentAdapter` binds `Student` objects to `item_student.xml` for the students list, and `LessonAdapter` binds `Lesson` objects to `item_lesson.xml` for the schedule list.
  * **Singleton Pattern:** `SharedPreferencesManager` ensures a single point of access to persisted data.&#x20;
  * **Manager Classes:** Separation of concerns between UI and business logic:

    * `ScheduleManager`: Contains all logic to group students, check instructor availability, avoid time overlaps, and create `Lesson` objects.
    * `SharedPreferencesManager`: Centralizes saving/loading of students and lessons.&#x20;
  * **Separation of Concerns:** The project distinguishes between the Activity (View), Models (Data), and Managers (Business Logic).

## The Challenge: Logic and Constraints

The algorithm at the core of this system was built to solve the following set of constraints as defined by the Asgard challenge:

* **Pool Availability:** Open Sunday through Thursday; closed on Fridays (treated as a weekend day).&#x20;

* **Lesson Duration:**

  * Private Lesson: 45 minutes&#x20;
  * Group Lesson: 60 minutes&#x20;

* **Instructor Constraints:**

  | Instructor | Available Days & Times                     | Teachable Styles                                |
  | :--------- | :----------------------------------------- | :---------------------------------------------- |
  | **Yotam**  | Monday & Thursday, 16:00-20:00             | Freestyle, Breaststroke, Butterfly, Backstroke  |
  | **Yoni**   | Tuesday, Wednesday & Thursday, 08:00-15:00 | Breaststroke & Butterfly only                   |
  | **Jonny**  | Sunday, Tuesday & Thursday, 10:00-19:00    | Freestyle, Breaststroke, Butterfly, Backstroke  |

* **Student Limits and Lesson Rules:**

  * Up to 30 students can be added.&#x20;
  * Group lessons require at least 2 students and can hold up to 6.&#x20;
  * Private lessons are one-on-one, lasting 45 minutes.&#x20;
  * Students specify swim style and lesson type preference; the algorithm only matches them with lessons that honor these preferences.
  * No lessons on Friday (pool closed).&#x20;

## How to Run the Project

1. Clone this repository.
2. Open the project in Android Studio.
3. Wait for the Gradle sync process to complete.
4. Run the application on an emulator or a physical Android device (min SDK level defined in `build.gradle`).

---

Developed by: This solution was submitted as part of a developer candidate challenge for Asgard Systems Ltd. No commercial use was made of this product.
