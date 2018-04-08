/*
 *    Copyright 2009-2012 The MyBatis Team
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package com.ibatis.dao.client.template;

import com.ibatis.dao.client.Dao;
import com.ibatis.dao.client.DaoManager;

/*
 * A base class for Dao implementations, or other DAO templates.
 */
public abstract class DaoTemplate implements Dao {

  protected DaoManager daoManager;

  /*
   * The DaoManager that manages this Dao instance will be passed
   * in as the parameter to this constructor automatically upon
   * instantiation.
   *
   * @param daoManager
   */
  public DaoTemplate(DaoManager daoManager) {
    this.daoManager = daoManager;
  }

}
