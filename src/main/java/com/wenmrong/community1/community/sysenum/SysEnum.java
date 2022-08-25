package com.wenmrong.community1.community.sysenum;

/**
 * @author wenmingrong@kungeek.com
 * @since
 */
public class SysEnum {

    public enum Status {
        // 状态：1、未付款，2、已付款，3、未发货，4、已发货，5、交易成功，6、交易关闭
       UNPAID(1), PAID(2),NOTSHIPPED(3),shipped(4),TRANSACTIONSUCCEEDED(5), TRANSACTIONCLOSED(6);
        private Integer status;

        Status(Integer status) {
            this.status = status;
        }

        public Integer getStatus() {
            return status;
        }

        public void setStatus(Integer status) {
            this.status = status;
        }
    }

    public enum CommentType {
        COMMENT(1),QUESTION(2);
        private Integer type;

        CommentType(Integer type) {
            this.type = type;
        }

        public Integer getType() {
            return type;
        }

        public void setType(Integer type) {
            this.type = type;
        }
    }

    public enum LEVEL {
        LV1(1),
        LV2(2),
        LV3(3),
        LV4(4),
        LV5(5),
        LV6(6),
        LV7(7),
        LV8(8);
        private Integer level;

        LEVEL(Integer level) {
            this.level = level;
        }

        public Integer getLevel() {
            return level;
        }

        public void setLevel(Integer level) {
            this.level = level;
        }
    }


}
