package com.jzo2o.customer.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jzo2o.common.model.PageResult;
import com.jzo2o.customer.model.domain.WorkerCertification;
import com.jzo2o.customer.model.dto.WorkerCertificationUpdateDTO;
import com.jzo2o.customer.model.dto.request.WorkerCertificationAuditAddReqDTO;
import com.jzo2o.customer.model.dto.request.WorkerCertificationAuditPageQueryReqDTO;
import com.jzo2o.customer.model.dto.response.WorkerCertificationAuditResDTO;

/**
 * <p>
 * 服务人员认证信息表 服务类
 * </p>
 *
 * @author itcast
 * @since 2023-09-06
 */
public interface IWorkerCertificationService extends IService<WorkerCertification> {



    /**
     * 根据服务人员id更新
     *
     * @param workerCertificationUpdateDTO 服务人员认证更新模型
     */
    void updateById(WorkerCertificationUpdateDTO workerCertificationUpdateDTO);

    /**
     * 服务端提交认证申请
     * @param workerCertificationAuditAddReqDTO
     * @return
     */
    WorkerCertification submitAuth(WorkerCertificationAuditAddReqDTO workerCertificationAuditAddReqDTO);

    PageResult<WorkerCertificationAuditResDTO> page(WorkerCertificationAuditPageQueryReqDTO workerCertificationAuditPageQueryReqDTO);

    void audit(Long id, Integer certificationStatus,String rejectReason);
}
