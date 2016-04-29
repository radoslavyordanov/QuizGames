package radoslav.yordanov.quizgames.model;

public class QuizAPI {
    private int quiz_id;
    private String quiz_image;
    private String quiz_type;
    private int choice_id;
    private String choice;
    private int is_right_choice;

    public String getChoice() {
        return choice;
    }

    public int getQuiz_id() {
        return quiz_id;
    }

    public String getQuiz_image() {
        return quiz_image;
    }

    public String getQuiz_type() {
        return quiz_type;
    }

    public int getChoice_id() {
        return choice_id;
    }

    public int getIs_right_choice() {
        return is_right_choice;
    }

}
