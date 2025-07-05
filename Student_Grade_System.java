import java.util.Scanner;

class StudentGradeSystem {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Input student name
        System.out.print("Enter student name: ");
        String name = scanner.nextLine();

        // Input marks for 5 subjects
        int[] marks = new int[5];
        String[] subjects = {"Math", "Science", "English", "History", "Computer"};

        int total = 0;
        for (int i = 0; i < marks.length; i++) {
            System.out.print("Enter marks for " + subjects[i] + ": ");
            marks[i] = scanner.nextInt();
            total += marks[i];
        }

        // Calculate average
        double average = total / 5.0;

        // Determine grade
        String grade;
        if (average >= 90) {
            grade = "A+";
        } else if (average >= 80) {
            grade = "A";
        } else if (average >= 70) {
            grade = "B";
        } else if (average >= 60) {
            grade = "C";
        } else if (average >= 50) {
            grade = "D";
        } else {
            grade = "F (Fail)";
        }

        // Output result
        System.out.println("\n------ Student Report ------");
        System.out.println("Name     : " + name);
        System.out.println("Total    : " + total);
        System.out.println("Average  : " + average);
        System.out.println("Grade    : " + grade);
    }
}
