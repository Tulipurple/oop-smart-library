package controller;

import model.Review;
import model.Space;
import model.Status;
import repository.ReviewRepository;
import repository.SpaceRepository;
import repository.StatusRepository;
import view.SpaceFormView;

import java.awt.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * 후기, 최신 상태를 등록하는 화면의 컨트롤러
 */
public class SpaceFormController {

    private final ReviewRepository reviewRepo;
    private final StatusRepository statusRepo;
    private final SpaceRepository  spaceRepo;

    private SpaceFormView view; // 등록 완료 후 피드백 전달용
    private String currentSpaceId; // 현재 등록 대상 공간 ID
    private List<Space> cachedSpaces = new ArrayList<>(); // 공간 목록 캐시

    // 생성자
    public SpaceFormController(ReviewRepository reviewRepo,
                               StatusRepository statusRepo,
                               SpaceRepository  spaceRepo) {
        this.reviewRepo = reviewRepo;
        this.statusRepo = statusRepo;
        this.spaceRepo  = spaceRepo;
    }

    // ───────────────────────────────────────────────────────
    // View 연결 및 초기화
    // ───────────────────────────────────────────────────────

    // View 등록 (순환 참조 방지를 위해 생성자 밖에서 설정)
    public void setView(SpaceFormView view) {
        this.view = view;
    }

    // 현재 등록 대상 공간 ID 설정 (드롭다운 선택 시 호출)
    public void setCurrentSpaceId(String spaceId) {
        this.currentSpaceId = spaceId;
    }

    // ───────────────────────────────────────────────────────
    // 공간 목록 제공
    // ───────────────────────────────────────────────────────

    // 공간 목록 반환 (호출 시 캐시 갱신)
    public List<String> getSpaceNames() {
        cachedSpaces = spaceRepo.findAll();
        List<String> names = new ArrayList<>();
        for (Space space : cachedSpaces) {
            names.add(space.getLocation() + " " + space.getName());
        }
        return names;
    }

    // spaceId 반환 (캐시 사용)
    public String getSpaceIdByIndex(int index) {
        if (index < 0 || index >= cachedSpaces.size()) return null;
        return cachedSpaces.get(index).getSpaceId();
    }
    
    // ───────────────────────────────────────────────────────
    // 이벤트 처리
    // ───────────────────────────────────────────────────────
   
    /**
     * View의 등록 버튼 클릭 시 호출
     * 선택된 항목만 저장하고 결과를 View에 전달
     */
    public void handleSubmit(boolean includeReview, boolean includeStatus,
                            int rating, String comment,
                            int noiseLevel, int crowdLevel, int cleanliness) {

        if (currentSpaceId == null || currentSpaceId.isBlank()) {
            if (view != null) {
                view.showFeedback("공간을 선택해 주세요.", Color.RED);
            }
            return;
        }

        // 후기 항목 검증
        if (includeReview && (rating < 1 || rating > 5)) {
            if (view != null) view.showFeedback("별점은 1~5 사이여야 합니다.", Color.RED);
            return;
        }

        // 후기 저장
        if (includeReview) {
            Review review = new Review(
                    generateId("RV"),
                    currentSpaceId,
                    rating,
                    comment,
                    LocalDateTime.now()
            );
            reviewRepo.save(review);
        }

        // 상태 저장
        if (includeStatus) {
            Status status = new Status(
                    generateId("ST"),
                    currentSpaceId,
                    noiseLevel,
                    crowdLevel,
                    cleanliness,
                    LocalDateTime.now()
            );
            statusRepo.save(status);
        }

        if (view != null) {
            view.resetForm();
            view.showFeedback("등록이 완료되었습니다!", Color.GREEN);
        }
    }

    // ───────────────────────────────────────────────────────
    // 유틸리티
    // ───────────────────────────────────────────────────────

    // ID 생성 헬퍼 (접두사 + UUID)
    private String generateId(String prefix) {
        String uid = UUID.randomUUID().toString()
                         .replace("-", "")
                         .substring(0, 8)
                         .toUpperCase();
        return prefix + "-" + uid;
    }
}