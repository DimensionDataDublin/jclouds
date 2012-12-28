/**
 * Licensed to jclouds, Inc. (jclouds) under one or more
 * contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  jclouds licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.jclouds.vcloud.director.v1_5.features;

import java.net.URI;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

import org.jclouds.Fallbacks.NullOnNotFoundOr404;
import org.jclouds.rest.annotations.Delegate;
import org.jclouds.rest.annotations.EndpointParam;
import org.jclouds.rest.annotations.Fallback;
import org.jclouds.rest.annotations.JAXBResponseParser;
import org.jclouds.rest.annotations.RequestFilters;
import org.jclouds.vcloud.director.v1_5.domain.org.Org;
import org.jclouds.vcloud.director.v1_5.domain.org.OrgList;
import org.jclouds.vcloud.director.v1_5.filters.AddVCloudAuthorizationAndCookieToRequest;
import org.jclouds.vcloud.director.v1_5.functions.href.OrgURNToHref;

import com.google.common.util.concurrent.ListenableFuture;

/**
 * @see OrgApi
 * @author Adrian Cole
 */
@RequestFilters(AddVCloudAuthorizationAndCookieToRequest.class)
public interface OrgAsyncApi {

   /**
    * @see OrgApi#list()
    */
   @GET
   @Path("/org/")
   @Consumes
   @JAXBResponseParser
   ListenableFuture<OrgList> list();

   /**
    * @see OrgApi#get(String)
    */
   @GET
   @Consumes
   @JAXBResponseParser
   @Fallback(NullOnNotFoundOr404.class)
   ListenableFuture<? extends Org> get(@EndpointParam(parser = OrgURNToHref.class) String orgUrn);

   /**
    * @see OrgApi#get(URI)
    */
   @GET
   @Consumes
   @JAXBResponseParser
   @Fallback(NullOnNotFoundOr404.class)
   ListenableFuture<? extends Org> get(@EndpointParam URI orgHref);
   
   /**
    * @return asynchronous access to {@link Metadata.Readable} features
    */
   @Delegate
   MetadataAsyncApi.Readable getMetadataApi(@EndpointParam(parser = OrgURNToHref.class) String orgUrn);

   @Delegate
   MetadataAsyncApi.Readable getMetadataApi(@EndpointParam URI orgHref);

}
