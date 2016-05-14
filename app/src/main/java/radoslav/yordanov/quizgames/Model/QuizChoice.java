/*
 *  Copyright 2016 Radoslav Yordanov
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package radoslav.yordanov.quizgames.model;

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
