package model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 후기 클래스
 */
public class Review {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    
    private final String reviewId; // 후기 고유 ID
    private final String spaceId; // 공간 ID
    private final int rating; // 별점 (1~5)
    private final String comment; // 한줄 후기
    private final LocalDateTime createdAt; // 작성 시간

    // 생성자
    public Review(String reviewId, String spaceId, int rating, String comment, LocalDateTime createdAt) {
        if (reviewId == null || reviewId.isBlank()) throw new IllegalArgumentException("reviewId는 비어있을 수 없습니다.");
        if (spaceId  == null || spaceId.isBlank())  throw new IllegalArgumentException("spaceId는 비어있을 수 없습니다.");
        if (rating < 1 || rating > 5)               throw new IllegalArgumentException("rating은 1~5 사이여야 합니다: " + rating);
        if (comment  == null)                        throw new IllegalArgumentException("comment는 null일 수 없습니다.");
        this.reviewId = reviewId;
        this.spaceId = spaceId;
        this.rating = rating;
        this.comment = comment;
        this.createdAt = createdAt;
    }

    // ───────────────────────────────────────────────────────
    // 직렬화 / 역직렬화
    // ───────────────────────────────────────────────────────

    // 객체 -> CSV 라인
    // CSV 저장 형식: reviewId,spaceId,rating,comment,createdAt
    public String toCsvLine() {
        return String.format("%s,%s,%d,%s,%s",
        reviewId, spaceId, rating, comment.replace(",", " "), createdAt.format(FORMATTER));
    }

    // CSV 라인 -> 객체
    public static Review fromCsvLine(String csvLine) {
        String[] parts = csvLine.split(",");
        if (parts.length < 5) {
            throw new IllegalArgumentException("잘못된 Review 형식: " + csvLine);
        }
        String reviewId = parts[0];
        String spaceId = parts[1];
        int rating = Integer.parseInt(parts[2]);
        String comment = parts[3];
        LocalDateTime createdAt = LocalDateTime.parse(parts[4], FORMATTER);
        return new Review(reviewId, spaceId, rating, comment, createdAt);
    }


    // ───────────────────────────────────────────────────────
    // Getter
    // ───────────────────────────────────────────────────────

    public String getReviewId() { return reviewId;}
    public String getSpaceId() { return spaceId;}
    public int getRating() { return rating;}
    public String getComment() { return comment;}
    public LocalDateTime getCreatedAt() { return createdAt;}

}
