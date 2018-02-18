/*
	Copyright 2018 Andre Schepers

	Licensed under the Apache License, Version 2.0 (the "License");
	you may not use this file except in compliance with the License.
	You may obtain a copy of the License at

	http://www.apache.org/licenses/LICENSE-2.0

	Unless required by applicable law or agreed to in writing, software
	distributed under the License is distributed on an "AS IS" BASIS,
	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
	See the License for the specific language governing permissions and
	limitations under the License.
*/
package eu.andreschepers.basewebapp.rest.dynamic_feature;

import eu.andreschepers.basewebapp.rest.annotation.Secure;
import org.keycloak.jaxrs.JaxrsBearerTokenFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.container.DynamicFeature;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.FeatureContext;

@Component
public class BearerAuthFeature implements DynamicFeature {

    private JaxrsBearerTokenFilter filter;

    @Autowired
    public BearerAuthFeature(JaxrsBearerTokenFilter filter) {
        this.filter = filter;
    }

    public void configure(ResourceInfo ri, FeatureContext ctx) {
        Secure methodAuth = ri.getResourceMethod().getAnnotation(Secure.class);
        Secure classAuth = ri.getResourceClass().getAnnotation(Secure.class);
        if (methodAuth == null && classAuth == null) return;
        ctx.register(filter);
    }
}