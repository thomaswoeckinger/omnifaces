/*
 * Copyright 2014 OmniFaces.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package org.omnifaces.el.functions;

import static org.omnifaces.util.Faces.getContext;
import static org.omnifaces.util.Faces.getELContext;

import java.util.Date;

import javax.el.ValueExpression;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import org.omnifaces.resourcehandler.GraphicResource;

/**
 * Collection of EL functions for working with components.
 *
 * @since 2.0
 * @author Arjan Tijms
 */
public final class Components {

	// Constructors ---------------------------------------------------------------------------------------------------

	private Components() {
		// Hide constructor.
	}

	// Utility --------------------------------------------------------------------------------------------------------

	/**
	 * Evaluates an attribute of a component by first checking if there's a value expression associated with it, and only if there isn't one
	 * look at a component property with that name.
	 * <p>
	 * The regular attribute collection ({@link UIComponent#getAttributes()}) does exactly the reverse; it looks at a component property
	 * first, then at the attribute collection and only looks at a value binding as the last option.
	 *
	 * @param component The component for which the attribute is to be evaluated
	 * @param name Name of attribute that is to be evaluated
	 * @return The value of the attribute, or null if either the component is null or if there's isn't an attribute by that name
	 */
	public static Object evalAttribute(UIComponent component, String name) {
		if (component == null) {
			return null;
		}

		ValueExpression valueExpression = component.getValueExpression(name);
		if (valueExpression != null) {
			return valueExpression.getValue(getELContext());
		} else {
			return component.getAttributes().get(name);
		}
	}

	/**
	 * <p>
	 * Returns <code>&lt;o:graphicImage&gt;</code> URL based on given expression string.
	 * <p>
	 * Usage example:
	 * <pre>
	 * &lt;a href="#{of:graphicImageURL('bean.getFullImage(image.id)')}"&gt;
	 *     &lt;o:graphicImage value="#{bean.getThumbnailImage(image.id)}" /&gt;
	 * &lt;/a&gt;
	 * </pre>
	 * @param expression Expression string representing the same value as you would use in
	 * <code>&lt;o:graphicImage&gt;</code>. It must be a quoted string. Any nested quotes can be escaped with backslash.
	 * @return <code>&lt;o:graphicImage&gt;</code> URL based on given expression string.
	 * @since 2.5
	 */
	public static String graphicImageURL(String expression) {
		return graphicImageURLWithTypeAndLastModified(expression, null, null);
	}

	/**
	 * <p>
	 * Returns <code>&lt;o:graphicImage&gt;</code> URL based on given expression string and image type.
	 * <p>
	 * Usage example:
	 * <pre>
	 * &lt;a href="#{of:graphicImageURL('bean.getFullImage(image.id)', 'png')}"&gt;
	 *     &lt;o:graphicImage value="#{bean.getThumbnailImage(image.id)}" type="png" /&gt;
	 * &lt;/a&gt;
	 * </pre>
	 * @param expression Expression string representing the same value as you would use in
	 * <code>&lt;o:graphicImage&gt;</code>. It must be a quoted string. Any nested quotes can be escaped with backslash.
	 * @param type The image type, represented as file extension.
	 * E.g. "jpg", "png", "gif", "ico", "svg", "bmp", "tiff", etc. This may be <code>null</code>.
	 * @return <code>&lt;o:graphicImage&gt;</code> URL based on given expression string and image type.
	 * @since 2.5
	 */
	public static String graphicImageURLWithType(String expression, String type) {
		return graphicImageURLWithTypeAndLastModified(expression, type, null);
	}

	/**
	 * <p>
	 * Returns <code>&lt;o:graphicImage&gt;</code> URL based on given expression string, image type and last modified.
	 * <p>
	 * Usage example:
	 * <pre>
	 * &lt;a href="#{of:graphicImageURL('bean.getFullImage(image.id)', 'png', image.lastModified)}"&gt;
	 *     &lt;o:graphicImage value="#{bean.getThumbnailImage(image.id)}" type="png" lastModified="#{image.lastModified}" /&gt;
	 * &lt;/a&gt;
	 * </pre>
	 * @param expression Expression string representing the same value as you would use in
	 * <code>&lt;o:graphicImage&gt;</code>. It must be a quoted string. Any nested quotes can be escaped with backslash.
	 * @param type The image type, represented as file extension.
	 * E.g. "jpg", "png", "gif", "ico", "svg", "bmp", "tiff", etc. This may be <code>null</code>.
	 * @param lastModified The "last modified" timestamp, can be either a {@link Long}, {@link Date}, or {@link String}
	 * which is parseable as {@link Long}. This may be <code>null</code>.
	 * @return <code>&lt;o:graphicImage&gt;</code> URL based on given expression string, image type and last modified.
	 * @since 2.5
	 */
	@SuppressWarnings("all") // Eclipse el-syntax.
	public static String graphicImageURLWithTypeAndLastModified(String expression, String type, Object lastModified) {
		FacesContext context = getContext();
		ValueExpression value = org.omnifaces.util.Components.createValueExpression("#{" + expression + "}", Object.class);
		GraphicResource resource = GraphicResource.create(context, value, type, lastModified);
		return context.getExternalContext().encodeResourceURL(resource.getRequestPath());
	}

}