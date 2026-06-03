package repository;

import model.Reservation;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 예약 데이터를 관리하는 Repository
 */
public class ReservationRepository extends BaseRepository<Reservation> {

    // 예약 데이터 파일 경로
    private static final String FILE_PATH = "data/reservations.csv";

    /**
     * 생성자
     */
    public ReservationRepository() {
        super(FILE_PATH);
    }

    /**
     * 파일에서 예약 데이터 로드
     *
     * CSV 형식:
     * reservationId,startTime,endTime,studentId,spaceId,status
     */
    @Override
    protected void loadFromFile() {

        for (String line : readLines()) {

            String[] parts = line.split(",");

            if (parts.length == 6) {

                long reservationId =
                        Long.parseLong(parts[0].trim());

                LocalDateTime startTime =
                        LocalDateTime.parse(parts[1].trim());

                LocalDateTime endTime =
                        LocalDateTime.parse(parts[2].trim());

                String studentId =
                        parts[3].trim();

                String spaceId =
                        parts[4].trim();

                String status =
                        parts[5].trim();

                dataList.add(
                        new Reservation(
                                reservationId,
                                startTime,
                                endTime,
                                studentId,
                                spaceId,
                                status
                        )
                );
            }
        }
    }

    /**
     * Reservation 객체를 CSV 문자열로 변환
     */
    @Override
    protected String toCsvLine(Reservation reservation) {
        return reservation.toCsvLine();
    }

    /**
     * 예약 추가
     */
    public synchronized void addReservation(
            Reservation reservation) {

        save(reservation);
    }

    /**
     * 특정 사용자의 예약 조회
     */
    public synchronized List<Reservation> findByStudentId(
            String studentId) {

        return findBy(
                reservation ->
                        reservation.getStudentId()
                                .equals(studentId)
        );
    }

    /**
     * 예약 ID로 조회
     */
    public synchronized Reservation findByReservationId(
            long reservationId) {

        List<Reservation> result =
                findBy(r ->
                        r.getReservationId()
                                == reservationId);

        if (result.isEmpty()) {
            return null;
        }

        return result.get(0);
    }
}