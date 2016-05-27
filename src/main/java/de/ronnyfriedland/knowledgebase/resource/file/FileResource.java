package de.ronnyfriedland.knowledgebase.resource.file;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import de.ronnyfriedland.knowledgebase.entity.Document;
import de.ronnyfriedland.knowledgebase.exception.DataException;
import de.ronnyfriedland.knowledgebase.freemarker.TemplateProcessor;
import de.ronnyfriedland.knowledgebase.repository.IRepository;
import de.ronnyfriedland.knowledgebase.resource.AbstractDocumentResource;
import de.ronnyfriedland.knowledgebase.resource.RepositoryMetadata;

@Path("/files")
@Component
@RolesAllowed("user")
public class FileResource extends AbstractDocumentResource {
    private static final Logger LOG = LoggerFactory.getLogger(FileResource.class);

    @Autowired
    @Qualifier("fs")
    private IRepository<byte[]> repository;

    @Autowired
    private TemplateProcessor templateProcessor;

    /**
     * Init
     *
     * @return the processed document template (empty)
     */
    @GET
    @Produces(MediaType.TEXT_HTML)
    public Response initFiles() {
        return Response.ok(templateProcessor.getProcessedTemplate("file.ftl", new HashMap<String, Object>())).build();
    }

    /**
     * Retrieve an existing document for the given key.
     *
     * @param key the unique key of the document
     * @return the processed document template with filled in data
     */
    @GET
    @Path("/{key}")
    @Produces(MediaType.TEXT_HTML)
    public Response loadDocument(final @PathParam("key") String key) {
        Map<String, Object> attributes = new HashMap<>();
        try {
            Document<byte[]> document = repository.getDocument(key);
            if (null == document) {
                return Response.status(404).entity("Document not found").build();
            }
            attributes.put("header", document.getHeader());
            attributes.put("message", document.getMessage());
            attributes.put("encrypted", document.isEncrypted());
            attributes.put("tags", StringUtils.arrayToDelimitedString(document.getTags(), ","));

            return Response.ok(templateProcessor.getProcessedTemplate("file.ftl", attributes)).build();
        } catch (DataException e) {
            LOG.error("Error getting document", e);
            throw new WebApplicationException(Response.status(500).entity("Error getting document").build());
        }
    }

    /**
     * Retrieve an existing document for the given key.
     *
     * @param key the unique key of the document
     * @return the plain document stored in backend
     */
    @GET
    @Path("/{key:.+}/raw")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response rawFile(final @PathParam("key") String key) {
        try {
            Document<byte[]> document = repository.getDocument(key);
            if (null == document) {
                return Response.status(404).entity("Document not found").build();
            }
            return Response.ok(document.getMessage())
                    .header("Content-Disposition", "attachment; filename=\"" + document.getHeader() + "\"").build();
        } catch (DataException e) {
            LOG.error("Error getting document", e);
            throw new WebApplicationException(Response.status(500).entity("Error getting document").build());
        }
    }

    /**
     * Returns the content of the repository
     *
     * @param key the id of the repository element
     * @return the content of the repository
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/metadata")
    public Response getRepositoryMetadata() {
        return getRepositoryMetadata("c:/daten");
    }

    /**
     * Returns the content of the repository
     *
     * @param key the id of the repository element
     * @return the content of the repository
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/metadata/{key:.+}")
    public Response getRepositoryMetadata(final @PathParam("key") String key) {
        try {
            RepositoryMetadata metadata = repository.getMetadata(key);
            return Response.ok(metadata).build();
        } catch (DataException e) {
            LOG.error("Error getting content", e);
            throw new WebApplicationException(Response.status(500).entity("Error getting document").build());
        }
    }
}