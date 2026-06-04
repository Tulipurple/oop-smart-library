package controller;

import model.Reservation;
import repository.ReservationRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 사용자의 현재 예약 / 지난 예약 조회를 담당하는 컨트롤러 
 */
public class MyReservationController {

    private final ReservationRepository reservationRepository;

    /**
     * 생성자
     */
    public MyReservationController() {
        this.reservationRepository = new ReservationRepository();
    }

    /**
     * 현재 예약 조회
     * 종료 시간이 현재 시간 이후인 예약만 반환
     */
    public synchronized List<Reservation> getActiveReservations(
            String studentId) {

        List<Reservation> allReservations =
                reservationRepository.findByStudentId(studentId);

        List<Reservation> activeReservations =
                new ArrayList<>();

        LocalDateTime now =
                LocalDateTime.now();

        for (Reservation reservation : allReservations) {

            if (reservation.getEndTime().isAfter(now)) {
                activeReservations.add(reservation);
            }
        }

        return activeReservations;
    }

    /**
     * 지난 예약 조회
     * 종료 시간이 현재 시간 이전인 예약만 반환
     */
    public synchronized List<Reservation> getPastReservations(
            String studentId) {

        List<Reservation> allReservations =
                reservationRepository.findByStudentId(studentId);

        List<Reservation> pastReservations =
                new ArrayList<>();

        LocalDateTime now =
                LocalDateTime.now();

        for (Reservation reservation : allReservations) {

            if (reservation.getEndTime().isBefore(now)) {
                pastReservations.add(reservation);
            }
        }

        return pastReservations;
    }

    /**
     * 예약 ID로 조회
     */
    public synchronized Reservation getReservation(long reservationId) {
       
        return reservationRepository.findByReservationId(String.valueOf(reservationId));
    }
}