/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jclouds.openstack.neutron.v2_0.functions;

import com.google.common.annotations.Beta;
import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.inject.TypeLiteral;
import org.jclouds.collect.IterableWithMarker;
import org.jclouds.collect.internal.Arg0ToPagedIterable;
import org.jclouds.http.functions.ParseJson;
import org.jclouds.json.Json;
import org.jclouds.openstack.keystone.v2_0.domain.PaginatedCollection;
import org.jclouds.openstack.neutron.v2_0.NeutronApi;
import org.jclouds.openstack.neutron.v2_0.domain.Network;
import org.jclouds.openstack.neutron.v2_0.features.NetworkApi;
import org.jclouds.openstack.neutron.v2_0.functions.ParseNetworkDetails.Networks;
import org.jclouds.openstack.v2_0.domain.Link;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.beans.ConstructorProperties;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.jclouds.openstack.v2_0.options.PaginationOptions.Builder.marker;

/**
 * @author Nick Livens
 */
@Beta
@Singleton
public class ParseNetworkDetails extends ParseJson<Networks> {
   static class Networks extends PaginatedCollection<Network> {

      @ConstructorProperties({ "networks", "networks_links" })
      protected Networks(Iterable<Network> networks, Iterable<Link> networks_links) {
         super(networks, networks_links);
      }

   }

   @Inject
   public ParseNetworkDetails(Json json) {
      super(json, TypeLiteral.get(Networks.class));
   }

   public static class ToPagedIterable extends Arg0ToPagedIterable.FromCaller<Network, ToPagedIterable> {

      private final NeutronApi api;

      @Inject
      protected ToPagedIterable(NeutronApi api) {
         this.api = checkNotNull(api, "api");
      }

      @Override
      protected Function<Object, IterableWithMarker<Network>> markerToNextForArg0(Optional<Object> arg0) {
         String zone = arg0.isPresent() ? arg0.get().toString() : null;
         final NetworkApi networkApi = api.getNetworkApiForZone(zone);
         return new Function<Object, IterableWithMarker<Network>>() {

            @SuppressWarnings("unchecked")
            @Override
            public IterableWithMarker<Network> apply(Object input) {
               return IterableWithMarker.class.cast(networkApi.listInDetail(marker(input.toString())));
            }

            @Override
            public String toString() {
               return "list()";
            }
         };
      }

   }

}
