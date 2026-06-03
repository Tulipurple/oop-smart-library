package repository;

import model.Review;

import java.util.List;

/**
 * 후기(Review) 데이터 관리 Repository 클래스
 */
public class ReviewRepository extends BaseRepository<Review> {
    
    private static final String FILE_PATH = "data/reviews.csv";

    public ReviewRepository() {
        super(FILE_PATH);
    }

    // ───────────────────────────────────────────────────────
    // 파일 I/O
    // ───────────────────────────────────────────────────────
   
    @Override
    protected void loadFromFile() {
        for (String line : readLines()) {
            try {
                dataList.add(Review.fromCsvLine(line));
            } catch (IllegalArgumentException e) {
                System.err.println("후기 데이터 로드 실패: " + e.getMessage());
            }
        }
    }

    @Override
    protected String toCsvLine(Review review) {
        return review.toCsvLine();
    }

    // ───────────────────────────────────────────────────────
    // 조회 메서드
    // ───────────────────────────────────────────────────────

    // 공간 ID로 후기 조회
    public List<Review> findBySpaceId(String spaceId) {
        return findBy(r -> r.getSpaceId().equals(spaceId));
    }   
}
