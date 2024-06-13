package com.jzo2o.customer.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jzo2o.api.customer.dto.response.AddressBookResDTO;
import com.jzo2o.common.model.PageResult;
import com.jzo2o.customer.model.domain.AddressBook;
import com.jzo2o.customer.model.dto.request.AddressBookPageQueryReqDTO;
import com.jzo2o.customer.model.dto.request.AddressBookUpsertReqDTO;
import com.jzo2o.customer.model.dto.response.AddressResDto;

import java.util.List;

/**
 * <p>
 * 地址薄 服务类
 * </p>
 *
 * @author itcast
 * @since 2023-07-06
 */
public interface IAddressBookService extends IService<AddressBook> {

    /**
     * 根据用户id和城市编码获取地址
     *
     * @param userId 用户id
     * @param cityCode 城市编码
     * @return 地址编码
     */
    List<AddressBookResDTO> getByUserIdAndCity(Long userId, String cityCode);

    /**
     * 新增地址薄
     * @param addressBookUpsertReqDTO
     */
    void add(AddressBookUpsertReqDTO addressBookUpsertReqDTO);

    /**
     * 分页查询地址薄
     * @param addressBookPageQueryReqDTO
     * @return
     */
    PageResult<AddressResDto> pageAddress(AddressBookPageQueryReqDTO addressBookPageQueryReqDTO);

    /**
     * 更新地址薄
     * @param id
     * @param addressBookUpsertReqDTO
     * @return
     */
    AddressBook update(Long id, AddressBookUpsertReqDTO addressBookUpsertReqDTO);

    /**
     *
     * @param id
     * @param flag
     */
    AddressBook setDefault(Long id, Integer flag);

    /**
     * 获取默认地址
     * @return
     */
    AddressBookResDTO getDefaultAddress();
}
