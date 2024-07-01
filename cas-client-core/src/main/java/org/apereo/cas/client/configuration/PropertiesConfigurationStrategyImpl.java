/**
 * Licensed to Apereo under one or more contributor license
 * agreements. See the NOTICE file distributed with this work
 * for additional information regarding copyright ownership.
 * Apereo licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License.  You may obtain a
 * copy of the License at the following location:
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apereo.cas.client.configuration;

import org.apereo.cas.client.util.CommonUtils;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * @author Scott Battaglia
 * @since 3.4.0
 */
public final class PropertiesConfigurationStrategyImpl extends BaseConfigurationStrategy {

    /**
     * Property name we'll use in the {@link jakarta.servlet.FilterConfig} and {@link jakarta.servlet.ServletConfig} to try and find where
     * you stored the configuration file.
     */
    private static final String CONFIGURATION_FILE_LOCATION = "configFileLocation";

    /**
     * Default location of the configuration file.  Mostly for testing/demo.  You will most likely want to configure an alternative location.
     */
    private static final String DEFAULT_CONFIGURATION_FILE_LOCATION = "/etc/java-cas-client.properties";

    private static final Logger LOGGER = LoggerFactory.getLogger(PropertiesConfigurationStrategyImpl.class);

    private final Properties properties = new Properties();

    private String simpleFilterName;

    @Override
    public void init(final FilterConfig filterConfig, final Class<? extends Filter> filterClazz) {
        this.simpleFilterName = filterClazz.getSimpleName();
        final var fileLocationFromFilterConfig = filterConfig.getInitParameter(CONFIGURATION_FILE_LOCATION);
        final var filterConfigFileLoad = loadPropertiesFromFile(fileLocationFromFilterConfig);

        if (!filterConfigFileLoad) {
            final var fileLocationFromServletConfig = filterConfig.getServletContext().getInitParameter(CONFIGURATION_FILE_LOCATION);
            final var servletContextFileLoad = loadPropertiesFromFile(fileLocationFromServletConfig);

            if (!servletContextFileLoad) {
                final var defaultConfigFileLoaded = loadPropertiesFromFile(DEFAULT_CONFIGURATION_FILE_LOCATION);
                CommonUtils.assertTrue(defaultConfigFileLoaded, "unable to load properties to configure CAS client");
            }
        }
    }

    @Override
    protected String get(final ConfigurationKey configurationKey) {
        final var property = configurationKey.getName();
        final var filterSpecificProperty = this.simpleFilterName + "." + property;

        final var filterSpecificValue = this.properties.getProperty(filterSpecificProperty);

        if (CommonUtils.isNotEmpty(filterSpecificValue)) {
            return filterSpecificValue;
        }

        return this.properties.getProperty(property);
    }

    private boolean loadPropertiesFromFile(final String file) {
        if (CommonUtils.isEmpty(file)) {
            return false;
        }
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
            this.properties.load(fis);
            return true;
        } catch (final IOException e) {
            LOGGER.warn("Unable to load properties for file {}", file, e);
            return false;
        } finally {
            CommonUtils.closeQuietly(fis);
        }
    }
}
