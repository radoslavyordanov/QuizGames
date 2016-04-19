package radoslav.yordanov.quizgames.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class QuizChoice implements Parcelable {
    private int choiceId;
    private String choice;
    private int isRightChoice;

    public QuizChoice() {

    }

    public int getChoiceId() {
        return choiceId;
    }

    public void setChoiceId(int choiceId) {
        this.choiceId = choiceId;
    }

    public String getChoice() {
        return choice;
    }

    public void setChoice(String choice) {
        this.choice = choice;
    }

    public int getIsRightChoice() {
        return isRightChoice;
    }

    public void setIsRightChoice(int isRightChoice) {
        this.isRightChoice = isRightChoice;
    }

    protected QuizChoice(Parcel in) {
        choiceId = in.readInt();
        choice = in.readString();
        isRightChoice = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(choiceId);
        dest.writeString(choice);
        dest.writeInt(isRightChoice);
    }

    public static final Parcelable.Creator<QuizChoice> CREATOR = new Parcelable.Creator<QuizChoice>() {
        @Override
        public QuizChoice createFromParcel(Parcel in) {
            return new QuizChoice(in);
        }

        @Override
        public QuizChoice[] newArray(int size) {
            return new QuizChoice[size];
        }
    };

}
