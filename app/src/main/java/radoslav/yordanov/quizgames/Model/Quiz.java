package radoslav.yordanov.quizgames.Model;

import java.util.ArrayList;

/**
 * Created by radoslav on 4/16/16.
 */
public class Quiz {
    private int quizId;
    private String quizImage;
    private String quizType;
    private ArrayList<QuizChoice> quizChoices;

    public Quiz() {
        quizChoices = new ArrayList<>();
    }

    public int getQuizId() {
        return quizId;
    }

    public void setQuizId(int quizId) {
        this.quizId = quizId;
    }

    public String getQuizImage() {
        return quizImage;
    }

    public void setQuizImage(String quizImage) {
        this.quizImage = quizImage;
    }

    public String getQuizType() {
        return quizType;
    }

    public void setQuizType(String quizType) {
        this.quizType = quizType;
    }

    public ArrayList<QuizChoice> getQuizChoices() {
        return quizChoices;
    }

    public void addQuizChoice(QuizChoice quizChoice) {
        quizChoices.add(quizChoice);
    }

}
