package com.jzo2o.customer.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jzo2o.customer.mapper.BankAccountMapper;
import com.jzo2o.customer.model.domain.BankAccount;
import com.jzo2o.customer.model.dto.request.BankAccountUpsertReqDTO;
import com.jzo2o.customer.model.dto.response.BankAccountResDTO;
import com.jzo2o.customer.service.IBankAccountService;
import com.jzo2o.mvc.utils.UserContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 个人银行账户 服务实现类
 * </p>
 *
 * @author sjb
 * @since 2024-06-11
 */
@Service
public class BankAccountServiceImpl extends ServiceImpl<BankAccountMapper, BankAccount> implements IBankAccountService {

    @Override
    @Transactional
    public BankAccount saveOrUpdate(BankAccountUpsertReqDTO bankAccountUpsertReqDTO,Integer type) {
        //1.获取当前登录用户id
        Long userId= UserContext.currentUserId();
        //2.将dto转为实体
        BankAccount bankAccount = BeanUtil.toBean(bankAccountUpsertReqDTO, BankAccount.class);
        bankAccount.setUserId(userId);
        bankAccount.setUserType(type);
        //3.保存或更新
        saveOrUpdate(bankAccount);
        return bankAccount;
    }

    @Override
    public BankAccountResDTO currentUserBankAccount(Integer type) {
        //1.获取当前登录用户id
        Long userId= UserContext.currentUserId();
        //2.查询当前登录用户的银行账户
        BankAccount bankAccount = lambdaQuery()
                .eq(BankAccount::getUserId, userId)
                .one();
        //3.将实体转为dto
        BankAccountResDTO bankAccountResDTO = BeanUtil.toBean(bankAccount, BankAccountResDTO.class);
        bankAccountResDTO.setId(userId);
        bankAccountResDTO.setType(type);
        return bankAccountResDTO;
    }
}
