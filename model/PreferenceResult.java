package model;

public class PreferenceResult {

    // 큰 요소
    private int quietPreference;
    private int conversationPreference;
    private int typingNeed;

    // 중간 요소
    private int openPreference;
    private int partitionPreference;
    private int socketNeed;

    // 낮은 요소
    private int sofaPreference;
    private int rollingChairPreference;
    private int normalChairPreference;

    public PreferenceResult() {
        clear();
    }

    public void clear() {
        quietPreference = 0;
        conversationPreference = 0;
        typingNeed = 0;

        openPreference = 0;
        partitionPreference = 0;
        socketNeed = 0;

        sofaPreference = 0;
        rollingChairPreference = 0;
        normalChairPreference = 0;
    }

    // 선택지 점수 누적
    public void accumulateScore(int questionIndex, int optionIndex) {

        switch (questionIndex) {

            case 0: // Q1
                if (optionIndex == 0) {
                    quietPreference += 5;
                    conversationPreference += 1;
                } else if (optionIndex == 1) {
                    partitionPreference += 3;
                    quietPreference += 2;
                } else if (optionIndex == 2) {
                    openPreference += 2;
                    conversationPreference += 1;
                } else if (optionIndex == 3) {
                    openPreference += 5;
                    conversationPreference += 5;
                }
                break;

            case 1: // Q2
                if (optionIndex == 0) {
                    partitionPreference += 3;
                    quietPreference += 2;
                } else if (optionIndex == 1) {
                    openPreference += 5;
                } else if (optionIndex == 2) {
                    openPreference += 2;
                }
                break;

            case 2: // Q3
                if (optionIndex == 0) {
                    quietPreference += 5;
                    partitionPreference += 3;
                } else if (optionIndex == 1) {
                    conversationPreference += 5;
                } else if (optionIndex == 2) {
                    conversationPreference += 5;
                    openPreference += 2;
                }
                break;

            case 3: // Q4
                if (optionIndex == 0) {
                    quietPreference += 5;
                    conversationPreference -= 5;
                } else if (optionIndex == 1) {
                    quietPreference += 3;
                    conversationPreference += 1;
                } else if (optionIndex == 3) {
                    conversationPreference += 5;
                    quietPreference += 2;
                }
                break;

            case 4: // Q5
                if (optionIndex == 0) {
                    socketNeed += 5;
                } else if (optionIndex == 1) {
                    quietPreference += 5;
                    conversationPreference -= 5;
                } else if (optionIndex == 2) {
                    sofaPreference += 2;
                    rollingChairPreference += 2;
                } else if (optionIndex == 3) {
                    typingNeed += 5;
                }
                break;

            case 5: // Q6
                if (optionIndex == 0) {
                    sofaPreference += 2;
                } else if (optionIndex == 1) {
                    rollingChairPreference += 2;
                } else if (optionIndex == 2) {
                    normalChairPreference += 2;
                }
                break;

            case 6: // Q7
                if (optionIndex == 1) {
                    typingNeed += 1;
                } else if (optionIndex == 2) {
                    typingNeed += 5;
                    socketNeed += 5;
                } else if (optionIndex == 3) {
                    typingNeed -= 5;
                    quietPreference += 4;
                }
                break;
        }
    }

    // 캡 적용
    public void finalizeResult() {

        quietPreference =
                Math.min(Math.max(quietPreference, -10), 10);

        conversationPreference =
                Math.min(Math.max(conversationPreference, -10), 10);

        typingNeed =
                Math.min(Math.max(typingNeed, -10), 10);

        openPreference =
                Math.min(Math.max(openPreference, -6), 6);

        partitionPreference =
                Math.min(Math.max(partitionPreference, -6), 6);

        socketNeed =
                Math.min(Math.max(socketNeed, -6), 6);

        sofaPreference =
                Math.min(Math.max(sofaPreference, 0), 3);

        rollingChairPreference =
                Math.min(Math.max(rollingChairPreference, 0), 3);

        normalChairPreference =
                Math.min(Math.max(normalChairPreference, 0), 3);
    }

    public int getQuietPreference() {
        return quietPreference;
    }

    public int getConversationPreference() {
        return conversationPreference;
    }

    public int getTypingNeed() {
        return typingNeed;
    }

    public int getOpenPreference() {
        return openPreference;
    }

    public int getPartitionPreference() {
        return partitionPreference;
    }

    public int getSocketNeed() {
        return socketNeed;
    }

    public int getSofaPreference() {
        return sofaPreference;
    }

    public int getRollingChairPreference() {
        return rollingChairPreference;
    }

    public int getNormalChairPreference() {
        return normalChairPreference;
    }
}