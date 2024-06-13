package com.jzo2o.customer.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jzo2o.common.model.PageResult;
import com.jzo2o.customer.mapper.WorkerAuditMapper;
import com.jzo2o.customer.mapper.WorkerCertificationMapper;
import com.jzo2o.customer.model.domain.WorkerAudit;
import com.jzo2o.customer.model.domain.WorkerCertification;
import com.jzo2o.customer.model.dto.WorkerCertificationUpdateDTO;
import com.jzo2o.customer.model.dto.request.WorkerCertificationAuditAddReqDTO;
import com.jzo2o.customer.model.dto.request.WorkerCertificationAuditPageQueryReqDTO;
import com.jzo2o.customer.model.dto.response.WorkerCertificationAuditResDTO;
import com.jzo2o.customer.service.IWorkerCertificationService;
import com.jzo2o.mvc.utils.UserContext;
import com.jzo2o.mysql.utils.PageHelperUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;

/**
 * <p>
 * 服务人员认证信息表 服务实现类
 * </p>
 *
 * @author itcast
 * @since 2023-09-06
 */
@Service
public class WorkerCertificationServiceImpl extends ServiceImpl<WorkerCertificationMapper, WorkerCertification> implements IWorkerCertificationService {

    /**
     * 根据服务人员id更新
     *
     * @param workerCertificationUpdateDTO 服务人员认证更新模型
     */
    @Override
    public void updateById(WorkerCertificationUpdateDTO workerCertificationUpdateDTO) {
        LambdaUpdateWrapper<WorkerCertification> updateWrapper = Wrappers.<WorkerCertification>lambdaUpdate()
                .eq(WorkerCertification::getId, workerCertificationUpdateDTO.getId())
                .set(WorkerCertification::getCertificationStatus, workerCertificationUpdateDTO.getCertificationStatus())
                .set(ObjectUtil.isNotEmpty(workerCertificationUpdateDTO.getName()), WorkerCertification::getName, workerCertificationUpdateDTO.getName())
                .set(ObjectUtil.isNotEmpty(workerCertificationUpdateDTO.getIdCardNo()), WorkerCertification::getIdCardNo, workerCertificationUpdateDTO.getIdCardNo())
                .set(ObjectUtil.isNotEmpty(workerCertificationUpdateDTO.getFrontImg()), WorkerCertification::getFrontImg, workerCertificationUpdateDTO.getFrontImg())
                .set(ObjectUtil.isNotEmpty(workerCertificationUpdateDTO.getBackImg()), WorkerCertification::getBackImg, workerCertificationUpdateDTO.getBackImg())
                .set(ObjectUtil.isNotEmpty(workerCertificationUpdateDTO.getCertificationMaterial()), WorkerCertification::getCertificationMaterial, workerCertificationUpdateDTO.getCertificationMaterial())
                .set(ObjectUtil.isNotEmpty(workerCertificationUpdateDTO.getCertificationTime()), WorkerCertification::getCertificationTime, workerCertificationUpdateDTO.getCertificationTime());
        super.update(updateWrapper);
    }

    @Override
    @Transactional
    public WorkerCertification submitAuth(WorkerCertificationAuditAddReqDTO workerCertificationAuditAddReqDTO) {
        //1.修改agency_certification表
        //1.1.获取服务人员id
        Long workerId = UserContext.currentUserId();
        //1.2.dto转换为实体
        WorkerCertification workerCertification = BeanUtil.toBean(workerCertificationAuditAddReqDTO, WorkerCertification.class);
        workerCertification.setId(workerId);
        //1.3.设置认证状态为认证中
        workerCertification.setCertificationStatus(1);
        //1.4.设置时间
        workerCertification.setCreateTime(LocalDateTime.now());
        workerCertification.setUpdateTime(LocalDateTime.now());
        //1.4.保存
        saveOrUpdate(workerCertification);
        //2.修改agency_audit表
        WorkerAudit workerAudit = new WorkerAudit();
        workerAudit.setId(workerId);
        workerAudit.setAuditStatus(0);
        workerAudit.setUpdateTime(LocalDateTime.now());
        workerAuditMapper.insert(workerAudit);
        return workerCertification;
    }

    @Resource
    private WorkerCertificationMapper workerCertificationMapper;
    @Override
    public PageResult<WorkerCertificationAuditResDTO> page(WorkerCertificationAuditPageQueryReqDTO workerCertificationAuditPageQueryReqDTO) {
        PageResult<WorkerCertificationAuditResDTO> pageResult = PageHelperUtils
                .selectPage(workerCertificationAuditPageQueryReqDTO, () -> workerCertificationMapper.queryWorkerCertification());
        return pageResult;
    }

    /**
     * 审核服务人员认证信息
     * @param id 认证信息id
     * @param certificationStatus 认证状态
     */
    @Resource
    private WorkerAuditMapper workerAuditMapper;
    @Override
    @Transactional
    public void audit(Long id, Integer certificationStatus,String rejectReason) {
        //1.更新worker_certification表
        LambdaUpdateWrapper<WorkerCertification> updateWrapper = Wrappers.<WorkerCertification>lambdaUpdate()
                .eq(WorkerCertification::getId, id)
                .set(WorkerCertification::getCertificationStatus, certificationStatus)
                .set(WorkerCertification::getUpdateTime, LocalDateTime.now());
        if(certificationStatus == 2){
            updateWrapper=updateWrapper.set(WorkerCertification::getCertificationTime, LocalDateTime.now());
        }
        update(updateWrapper);
        //2.更新worker_audit表
        WorkerAudit workerAudit = workerAuditMapper.selectById(id);
        if(workerAudit == null) {
            workerAudit = new WorkerAudit();
            workerAudit.setId(id);
            workerAuditMapper.insert(workerAudit);
        }
        workerAudit.setAuditStatus(1);
        workerAudit.setRejectReason(rejectReason);
        workerAudit.setAuditTime(LocalDateTime.now());
        workerAudit.setUpdateTime(LocalDateTime.now());
        workerAudit.setAuditorId(UserContext.currentUserId());
        workerAudit.setAuditorName(UserContext.currentUser().getName());
        workerAuditMapper.updateById(workerAudit);
    }
}
