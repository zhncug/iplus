package com.iplus.wechat.common.service;

import com.iplus.wechat.common.repository.BaseRepository;

import java.io.Serializable;

public interface BaseService<D, Q, I extends Serializable> extends BaseRepository<D, Q, I> {
}
