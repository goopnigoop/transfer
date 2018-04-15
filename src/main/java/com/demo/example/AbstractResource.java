package com.demo.example;

import org.modelmapper.ModelMapper;

import static com.demo.example.utils.GenericUtils.getGenericParameterClass;

/**
 * The type Abstract resource.
 *
 * @param <R> the type parameter
 * @param <T> the type parameter
 */
public abstract class AbstractResource<R, T> implements DtoMappable<R, T> {

    private ModelMapper modelMapper;
    private final Class<T> typeParameterClassDto;
    /**
     * The Type parameter class.
     */
    final Class<R> typeParameterClass;

    /**
     * Instantiates a new Abstract resource.
     */
    AbstractResource() {
        this.modelMapper = new ModelMapper();
        this.typeParameterClass = getGenericParameterClass(this.getClass(), 0);
        this.typeParameterClassDto = getGenericParameterClass(this.getClass(), 1);

    }

    @Override
    public T getDto(R r) {
        return modelMapper.map(r, typeParameterClassDto);
    }

    @Override
    public R getByDto(T t) {
        return modelMapper.map(t, typeParameterClass);
    }
}
