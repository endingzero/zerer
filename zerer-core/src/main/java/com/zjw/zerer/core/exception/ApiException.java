package com.zjw.zerer.core.exception;

import com.zjw.zerer.core.util.EnumCode;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class ApiException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  private Integer code;

  public ApiException() {}

  public ApiException(EnumCode resultEnum) {
    super(resultEnum.getMsg());
    this.code = resultEnum.getCode();
  }
  
  public ApiException(Integer code, String msg) {
    super(msg);
    this.code = code;
  }

}
