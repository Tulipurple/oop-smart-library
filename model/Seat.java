package model;

/**
 * 좌석 클래스
 */
public class Seat extends Space {
    // 좌석 고유 속성 - 성향테스트 결정 후 추가 예정
    private final int quietLevel;         // 조용함 관련
    private final int conversationLevel;  // 대화 가능 여부
    private final int openLevel;          // 개방감
    private final int partitionLevel;     // 칸막이 여부
    private final int typingLevel;        // 타이핑 가능 여부
    private final int socketLevel;        // 콘센트 여부
    private final int sofaLevel;          // 쇼파 의자 여부 (O: 2, X: 0)
    private final int rollingChairLevel;  // 바퀴 의자 여부 (O: 2, X: 0)
    private final int normalChairLevel;   // 일반 의자 여부 (O: 2, X: 0)

    // 생성자
    public Seat(String spaceId, String name, String location,
                int quietLevel, int conversationLevel, int openLevel,
                int partitionLevel, int typingLevel, int socketLevel,
                int sofaLevel, int rollingChairLevel, int normalChairLevel) {
        super(spaceId, name, location);
        this.quietLevel        = quietLevel;
        this.conversationLevel = conversationLevel;
        this.openLevel         = openLevel;
        this.partitionLevel    = partitionLevel;
        this.typingLevel       = typingLevel;
        this.socketLevel       = socketLevel;
        this.sofaLevel         = sofaLevel;
        this.rollingChairLevel = rollingChairLevel;
        this.normalChairLevel  = normalChairLevel;
    }

    @Override
    public SpaceType getSpaceType() {
        return SpaceType.SEAT;
    }

    @Override
    public String toCsvLine() {
        return String.join(",",
                getSpaceType().name(),
                getSpaceId(),
                getName(),
                getLocation(),
                String.valueOf(quietLevel),
                String.valueOf(conversationLevel),
                String.valueOf(openLevel),
                String.valueOf(partitionLevel),
                String.valueOf(typingLevel),
                String.valueOf(socketLevel),
                String.valueOf(sofaLevel),
                String.valueOf(rollingChairLevel),
                String.valueOf(normalChairLevel)
        );
    }

    // Getters
    public int getQuietLevel()        { return quietLevel;        }
    public int getConversationLevel() { return conversationLevel; }
    public int getOpenLevel()         { return openLevel;         }
    public int getPartitionLevel()    { return partitionLevel;    }
    public int getTypingLevel()       { return typingLevel;       }
    public int getSocketLevel()       { return socketLevel;       }
    public int getSofaLevel()         { return sofaLevel;         }
    public int getRollingChairLevel() { return rollingChairLevel; }
    public int getNormalChairLevel()  { return normalChairLevel;  }
}