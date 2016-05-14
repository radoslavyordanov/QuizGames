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
