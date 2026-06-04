package model;

import repository.SpaceRepository;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Recommender {

    private final SpaceRepository spaceRepository;

    public Recommender(SpaceRepository spaceRepository) {
        this.spaceRepository = spaceRepository;
    }

    // 성향 점수 기반 상위 3개 좌석 산출
    public List<Space> recommendSpaces(PreferenceResult result) {
        // 팀원 C분의 전용 메서드 규격을 사용하여 SEAT 타입 공간 정보만 추출
        List<Space> allSeats = spaceRepository.findAllSeats();
        List<ScoredSpace> scoredSpaces = new ArrayList<>();

        for (Space space : allSeats) {
            if (space instanceof Seat) {
                Seat seat = (Seat) space;
                double score = calculateScore(result, seat);
                scoredSpaces.add(new ScoredSpace(seat, score));
            }
        }

        // 점수 기준 내림차순 정렬
        Collections.sort(scoredSpaces);

        // 상위 3개 추출
        List<Space> recommended = new ArrayList<>();
        int limit = Math.min(3, scoredSpaces.size());
        for (int i = 0; i < limit; i++) {
            recommended.add(scoredSpaces.get(i).getSpace());
        }

        return recommended;
    }

    // 기획서 명세 수식 기반 매칭 점수 합산 연산
    private double calculateScore(PreferenceResult result, Seat seat) {
        double score = 0;

        score += result.getQuietPreference() * seat.getQuietLevel();
        score += result.getConversationPreference() * seat.getConversationLevel();
        score += result.getPartitionPreference() * seat.getPartitionLevel();
        score += result.getOpenPreference() * seat.getOpenLevel();
        score += result.getTypingNeed() * seat.getTypingLevel();
        score += result.getSocketNeed() * seat.getSocketLevel();
        score += result.getSofaPreference() * seat.getSofaLevel();
        score += result.getRollingChairPreference() * seat.getRollingChairLevel();
        score += result.getNormalChairPreference() * seat.getNormalChairLevel();

        return score;
    }

    // 정렬 처리를 위한 내부 정렬용 래퍼 클래스
    private static class ScoredSpace implements Comparable<ScoredSpace> {

        private final Seat seat;
        private final double score;

        public ScoredSpace(Seat seat, double score) {
            this.seat = seat;
            this.score = score;
        }

        public Seat getSpace() {
            return seat;
        }

        @Override
        public int compareTo(ScoredSpace other) {
            return Double.compare(other.score, this.score);
        }
    }
}