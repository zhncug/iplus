package com.iplus.common.service;

import com.iplus.common.repository.BaseRepository;

import java.io.Serializable;

public interface BaseService<D, Q, I extends Serializable> extends BaseRepository<D, Q, I> {
}
