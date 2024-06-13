package com.jzo2o.customer.controller.consumer;

import com.jzo2o.api.customer.dto.response.AddressBookResDTO;
import com.jzo2o.common.model.PageResult;
import com.jzo2o.customer.model.domain.AddressBook;
import com.jzo2o.customer.model.dto.request.AddressBookPageQueryReqDTO;
import com.jzo2o.customer.model.dto.request.AddressBookUpsertReqDTO;
import com.jzo2o.customer.model.dto.response.AddressResDto;
import com.jzo2o.customer.service.IAddressBookService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author SJB
 * @author 1.0
 * @description TODO
 * @date 2024/6/10 18:34
 */
@RestController("addressBookController")
@RequestMapping("/consumer/address-book")
@Api(tags = "用户端 - 地址薄相关接口")
public class AddressBookController {
    @Resource
    private IAddressBookService addressBookService;
    /**
     * 新增地址薄
     * @param addressBookUpsertReqDTO
     */
    @PostMapping
    @ApiOperation("新增地址薄")
    public void add(@RequestBody AddressBookUpsertReqDTO addressBookUpsertReqDTO) {
        addressBookService.add(addressBookUpsertReqDTO);
    }

    /**
     * 分页查询地址薄
     * @param addressBookPageQueryReqDTO
     * @return
     */
    @GetMapping("/page")
    @ApiOperation("分页查询地址薄")
    public PageResult<AddressResDto> page(AddressBookPageQueryReqDTO addressBookPageQueryReqDTO) {
        return addressBookService.pageAddress(addressBookPageQueryReqDTO);
    }

    /**
     * 根据id查询地址薄
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    @ApiOperation("根据id查询地址薄")
    public AddressBook getById(@PathVariable("id") Long id) {
        AddressBook addressBook = addressBookService.getById(id);
        return addressBook;
    }


    /**
     * 修改地址薄
     * @param id
     * @param addressBookUpsertReqDTO
     */
    @PutMapping("/{id}")
    @ApiOperation("修改地址薄")
    public AddressBook update(@PathVariable("id") Long id, @RequestBody AddressBookUpsertReqDTO addressBookUpsertReqDTO) {
        AddressBook update = addressBookService.update(id, addressBookUpsertReqDTO);
        return update;
    }

    /**
     * 删除地址薄
     * @param ids
     */
    @DeleteMapping("/batch")
    @ApiOperation("批量删除地址薄")
    public void deleteBatch(@RequestBody List<Long> ids) {
        addressBookService.removeByIds(ids);
    }

    /**
     * 设置默认地址
     * @param id
     * @param flag
     */
    @PutMapping("/default")
    @ApiOperation("设置默认地址")
    public AddressBook setDefault(@RequestParam("id") Long id, @RequestParam("flag") Integer flag){
        return addressBookService.setDefault(id, flag);
    }

    /**
     * 获取默认地址
     * @return
     */
    @GetMapping("/defaultAddress")
    @ApiOperation("获取默认地址")
    public AddressBookResDTO getDefaultAddress(){
        return addressBookService.getDefaultAddress();
    }

}
