package model;

import java.time.LocalDateTime;

/**
 * 예약 정보를 저장하는 모델 클래스
 *
 * ERD 기준 컬럼
 * reservation_id
 * start_time
 * end_time
 * student_id
 * space_id
 * status
 */
public class Reservation {

    // 예약 고유 번호
    private final long reservationId;

    // 예약 시작 시간
    private final LocalDateTime startTime;

    // 예약 종료 시간
    private final LocalDateTime endTime;

    // 예약자 학번
    private final String studentId;

    // 예약 공간 ID
    // 팀원 C의 Space 모델과 동일하게 String 사용
    private final String spaceId;

    // 예약 상태
    // 예: 예약중, 사용중, 완료
    private final String status;

    /**
     * 예약 생성자
     *
     * @param reservationId 예약 ID
     * @param startTime 시작 시간
     * @param endTime 종료 시간
     * @param studentId 예약자 학번
     * @param spaceId 공간 ID
     * @param status 예약 상태
     */
    public Reservation(long reservationId,
                       LocalDateTime startTime,
                       LocalDateTime endTime,
                       String studentId,
                       String spaceId,
                       String status) {

        this.reservationId = reservationId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.studentId = studentId;
        this.spaceId = spaceId;
        this.status = status;
    }

    /**
     * 예약 ID 반환
     */
    public long getReservationId() {
        return reservationId;
    }

    /**
     * 시작 시간 반환
     */
    public LocalDateTime getStartTime() {
        return startTime;
    }

    /**
     * 종료 시간 반환
     */
    public LocalDateTime getEndTime() {
        return endTime;
    }

    /**
     * 예약자 학번 반환
     */
    public String getStudentId() {
        return studentId;
    }

    /**
     * 공간 ID 반환
     */
    public String getSpaceId() {
        return spaceId;
    }

    /**
     * 예약 상태 반환
     */
    public String getStatus() {
        return status;
    }

    /**
     * CSV 저장용 문자열 반환
     *
     * 형식:
     * reservationId,startTime,endTime,studentId,spaceId,status
     */
    public String toCsvLine() {
        return reservationId + ","
                + startTime + ","
                + endTime + ","
                + studentId + ","
                + spaceId + ","
                + status;
    }

    @Override
    public String toString() {
        return "Reservation{" +
                "reservationId=" + reservationId +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", studentId='" + studentId + '\'' +
                ", spaceId='" + spaceId + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}