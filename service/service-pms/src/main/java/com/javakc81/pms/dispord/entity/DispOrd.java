package com.javakc81.pms.dispord.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "pms_disp_ord")
@EntityListeners(AuditingEntityListener.class)
public class DispOrd {

    @Id
    @Column(name = "id")
    @GeneratedValue(generator = "generator_uuid")
    @GenericGenerator(name = "generator_uuid", strategy = "uuid")
    @ApiModelProperty(value = "主键,采用hibernate的uuid生成32位字符串")
    private String id;

    @Column(name = "order_name")
    @ApiModelProperty(value = "指令名称")
    private String orderName;

    @Column(name = "spec_type")
    @ApiModelProperty(value = "指令类型")
    private int specType;

    @Column(name = "priority")
    @ApiModelProperty(value = "指令优先级")
    private String priority;

    @Column(name = "order_desc")
    @ApiModelProperty(value = "指令描述")
    private String orderDesc;

    @CreatedDate
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss", timezone = "GMT+8")
    @Column(name = "gmt_create", nullable = false, updatable = false)
    @ApiModelProperty(value = "创建时间")
    private Date gmtCreate;

    @LastModifiedDate
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss", timezone = "GMT+8")
    @Column(name = "gmt_modified", nullable = false, insertable = false)
    @ApiModelProperty(value = "修改时间")
    private Date gmtModified;

}