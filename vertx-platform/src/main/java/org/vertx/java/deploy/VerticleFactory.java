/*
 * Copyright 2011-2012 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.vertx.java.deploy;


import java.util.ServiceLoader;

import org.vertx.java.deploy.impl.VerticleManager;

/**
 * @author <a href="http://tfox.org">Tim Fox</a>
 */
public interface VerticleFactory {
	
  public static final Iterable<VerticleFactory> factories = ServiceLoader.load(VerticleFactory.class);

  void init(VerticleManager manager);
  
  String getLanguage();
  
  boolean isFactoryFor(String main);

  Verticle createVerticle(String main, ClassLoader parentCL) throws Exception;

  void reportException(Throwable t);

}