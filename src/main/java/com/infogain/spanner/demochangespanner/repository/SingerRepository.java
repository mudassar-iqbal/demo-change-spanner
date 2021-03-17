package com.infogain.spanner.demochangespanner.repository;

import org.springframework.cloud.gcp.data.spanner.repository.SpannerRepository;
import org.springframework.stereotype.Repository;

import com.google.cloud.spanner.Key;
import com.infogain.spanner.demochangespanner.entity.Singer;

@Repository
public interface SingerRepository extends SpannerRepository<Singer, Key>{

	
}
