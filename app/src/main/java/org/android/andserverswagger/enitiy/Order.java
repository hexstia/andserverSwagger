/**
 *  Copyright 2016 SmartBear Software
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package org.android.andserverswagger.enitiy;

import io.swagger.annotations.*;

import java.util.Date;

import javax.xml.bind.annotation.*;

@XmlRootElement(name = "Order")
@ApiModel(value = "Order", description = "Pet object that needs to be added to the store")
public class Order {
  @ApiModelProperty(required = true, value = "这是一个ID值")
  private long id;
  @ApiModelProperty(required = true)
  private long petId;
  @ApiModelProperty(required = true)
  private int quantity;
  @ApiModelProperty(required = true)
  private Date shipDate;
  @ApiModelProperty(required = true)
  private String status;
  @ApiModelProperty(required = true)
  private boolean complete;

  @XmlElement(name = "id")
  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public boolean isComplete() {
    return complete;
  }

  public void setComplete(boolean complete) {
    this.complete = complete;
  }

  @XmlElement(name = "petId")
  public long getPetId() {
    return petId;
  }

  public void setPetId(long petId) {
    this.petId = petId;
  }

  @XmlElement(name = "quantity")
  public int getQuantity() {
    return quantity;
  }

  public void setQuantity(int quantity) {
    this.quantity = quantity;
  }

  @XmlElement(name = "status")
  @ApiModelProperty(value = "Order Status", allowableValues = "placed, approved, delivered")
  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  @XmlElement(name = "shipDate")
  public Date getShipDate() {
    return shipDate;
  }

  public void setShipDate(Date shipDate) {
    this.shipDate = shipDate;
  }
}