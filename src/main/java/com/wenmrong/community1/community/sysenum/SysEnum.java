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

}
