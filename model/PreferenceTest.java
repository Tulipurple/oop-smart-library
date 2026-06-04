package model;

import java.util.ArrayList;
import java.util.List;

public class PreferenceTest {

    public static final int QUESTION_COUNT = 7;

    private final List<Question> questions;

    public PreferenceTest() {
        questions = new ArrayList<>();
        initQuestions();
    }

    private void initQuestions() {

        questions.add(new Question(
                "Q1. 공부할 때 가장 중요하게 생각하는 요소는 무엇인가요?",
                new String[]{
                        "a. 조용한 환경",
                        "b. 집중 가능한 분위기",
                        "c. 적당한 활기",
                        "d. 자유로운 분위기"
                }));

        questions.add(new Question(
                "Q2. 어떤 공간에서 가장 집중이 잘 되나요?",
                new String[]{
                        "a. 칸막이가 있는 개인석",
                        "b. 오픈형 좌석",
                        "c. 창가나 밝은 자리",
                        "d. 어디든 상관 없음"
                }));

        questions.add(new Question(
                "Q3. 주로 어떤 식으로 공부하시나요?",
                new String[]{
                        "a. 혼자 조용히",
                        "b. 친구와 함께",
                        "c. 팀플/토론 중심",
                        "d. 상황 따라 다름"
                }));

        questions.add(new Question(
                "Q4. 공부할 때 주변 소음에 얼마나 민감한가요?",
                new String[]{
                        "a. 아주 민감함",
                        "b. 조금 민감함",
                        "c. 크게 상관 없음",
                        "d. 약간의 소음이 좋음"
                }));

        questions.add(new Question(
                "Q5. 좌석 선택 시 가장 중요하게 보는 것은?",
                new String[]{
                        "a. 콘센트",
                        "b. 조용함",
                        "c. 좌석 편안함",
                        "d. 타이핑 가능 여부"
                }));

        questions.add(new Question(
                "Q6. 공부할 때 선호하는 좌석 형태는?",
                new String[]{
                        "a. 쇼파형",
                        "b. 바퀴 의자",
                        "c. 일반 의자",
                        "d. 상관 없음"
                }));

        questions.add(new Question(
                "Q7. 공부할 때 노트북을 얼마나 사용하시나요?",
                new String[]{
                        "a. 화면 시청 위주",
                        "b. 가벼운 타이핑",
                        "c. 지속적인 타이핑",
                        "d. 사용하지 않음"
                }));
    }

    public List<Question> getQuestions() {
        return new ArrayList<>(questions);
    }

    public int getQuestionCount() {
        return questions.size();
    }

    public static class Question {

        private final String query;
        private final String[] options;

        public Question(String query, String[] options) {
            this.query = query;
            this.options = options.clone();
        }

        public String getQuery() {
            return query;
        }

        public String[] getOptions() {
            return options.clone();
        }
    }
}