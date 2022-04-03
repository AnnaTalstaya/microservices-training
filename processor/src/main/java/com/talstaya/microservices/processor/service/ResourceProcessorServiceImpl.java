package com.talstaya.microservices.processor.service;

import ch.qos.logback.core.util.Duration;
import com.talstaya.microservices.processor.dto.Mp3DTO;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.parser.mp3.Mp3Parser;
import org.apache.tika.sax.BodyContentHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Service
public class ResourceProcessorServiceImpl implements ResourceProcessorService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ResourceProcessorServiceImpl.class);

    @Override
    public Mp3DTO parsingMp3(MultipartFile mp3) {

        Metadata metadata = new Metadata();
        Parser mp3Parser = new Mp3Parser();
        try {
            mp3Parser.parse(mp3.getInputStream(), new BodyContentHandler(), metadata, new ParseContext());
        } catch (IOException | SAXException | TikaException e) {
            LOGGER.error("Error occurred while parsing mp3 file", e);
        }

        String lengthInHHMMSS = null;
        if (metadata.get("xmpDM:duration") != null) {
            long mp3Milliseconds = Duration.buildBySeconds(Double.parseDouble(metadata.get("xmpDM:duration"))).getMilliseconds();
            lengthInHHMMSS = formatTimeLength(mp3Milliseconds);
        }

        return Mp3DTO.builder()
                .name(metadata.get("title"))
                .artist(metadata.get("xmpDM:artist"))
                .album(metadata.get("xmpDM:album"))
                .length(lengthInHHMMSS)
                .year(metadata.get("xmpDM:releaseDate"))
                .build();
    }

    private String formatTimeLength(long milliseconds) {
        long HH = TimeUnit.MILLISECONDS.toHours(milliseconds);
        long MM = TimeUnit.MILLISECONDS.toMinutes(milliseconds) % 60;
        long SS = TimeUnit.MILLISECONDS.toSeconds(milliseconds) % 60;
        return String.format("%02d:%02d:%02d", HH, MM, SS);
    }
}
