package uz.pdp.railway.service;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import uz.pdp.railway.model.dto.NewsDto;
import uz.pdp.railway.model.entity.NewsEntity;
import uz.pdp.railway.model.repository.NewsRepository;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class NewsService {

    private final static String PARENT_URL = "./src/main/resources/static/news_img/";
    private final static String IMAGE_PNG = "image/png";
    private final static String IMAGE_JPG = "image/jpg";

    private final NewsRepository newsRepository;

    public NewsService(NewsRepository userRepository) {
        this.newsRepository = userRepository;
    }

    public void addNews(final NewsDto newsDto) throws IOException {
        final String imageUrl = saveImage(newsDto.getImage());
        final NewsEntity newsEntity = new NewsEntity();
        newsEntity.setText(newsDto.getText());
        newsEntity.setUrl(imageUrl);
        newsEntity.setTitle(newsDto.getTitle());
        newsRepository.save(newsEntity);
    }

    public NewsEntity getNews(final String newsId) {
        Optional<NewsEntity> news = newsRepository.findById(Long.parseLong(newsId));
        return news.orElse(new NewsEntity());
    }

    private String saveImage(final MultipartFile multipartFile) throws IOException {
        final File file = new File(PARENT_URL);
        if (file.isDirectory() && file.exists()) {
            final String URL = "/img/" + getImageUrl() + getContentType(multipartFile.getContentType());
            final File file1 = new File(file, URL);
            file1.createNewFile();
            writeImage(file1, multipartFile);
            return URL;
        } else {
            throw new FileNotFoundException();
        }
    }

    private String getContentType(final String contentType) {
        if (contentType.equals(IMAGE_JPG)) {
            return ".jpg";
        } else if (contentType.equals(IMAGE_PNG)) {
            return ".png";
        }
        return null;
    }

    private String getImageUrl() {
        return UUID.randomUUID().toString();
    }

    private void writeImage(final File file, final MultipartFile multipartFile) throws IOException {
        final FileOutputStream fileOutputStream = new FileOutputStream(file);
        fileOutputStream.write(multipartFile.getBytes());
        fileOutputStream.close();
    }

    public List<NewsEntity> getNewsPage(Integer pageSize) {
        Pageable page = PageRequest.of(pageSize - 1, 3, Sort.by("id").descending());
        return newsRepository.findAll(page).stream().toList();
    }
}
