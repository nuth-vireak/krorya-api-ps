package com.kshrd.krorya.service.serviceImplementation;

import com.kshrd.krorya.convert.IngredientDTOConvertor;
import com.kshrd.krorya.exception.CustomNotFoundException;
import com.kshrd.krorya.exception.DuplicatedException;
import com.kshrd.krorya.model.dto.IngredientDTO;
import com.kshrd.krorya.model.entity.AppUser;
import com.kshrd.krorya.model.entity.CustomUserDetail;
import com.kshrd.krorya.model.entity.Ingredient;
import com.kshrd.krorya.model.request.IngredientRequest;
import com.kshrd.krorya.repository.IngredientRepository;
import com.kshrd.krorya.service.IngredientService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class IngredientServiceImpl implements IngredientService {

    private static final Logger log = LoggerFactory.getLogger(IngredientServiceImpl.class);
    private final IngredientRepository ingredientRepository;
    private final IngredientDTOConvertor ingredientDTOConvertor;

    @Override
    public List<IngredientDTO> getIngredients() {
        List<Ingredient> ingredients = ingredientRepository.getIngredientsWithNullUserId();
        return ingredientDTOConvertor.convertIngredientListToIngredientDTOList(ingredients);
    }

    @Override
    @Transactional
    public IngredientDTO getIngredientByNameOrCreate(String name) {

        UUID currentUserId = getCurrentUserId();

        // Check for ingredient with the given name and user_id as null
        Optional<Ingredient> optionalIngredientNullUser = ingredientRepository.findByNameAndUserIdIsNull(name);

        if (optionalIngredientNullUser.isPresent()) {
            return ingredientDTOConvertor.convertIngredientToIngredientDTO(optionalIngredientNullUser.get());
        }

        // Check for ingredient with the given name and current user_id
        Optional<Ingredient> optionalIngredientWithUser = ingredientRepository.findByNameAndUserId(name, currentUserId);

        if (optionalIngredientWithUser.isPresent()) {
            throw new DuplicatedException("Ingredient with name '" + name + "' already exists for the current user.");
        }

        Ingredient newIngredient = Ingredient.builder()
                .ingredientName(name)
                .userId(currentUserId)
                .ingredientIcon("074491e7-27b7-43ed-97d4-1dee238564b2.jpg")
                .ingredientType("uncategorized")
                .build();

        ingredientRepository.insertIngredient(newIngredient);

        return ingredientDTOConvertor.convertIngredientToIngredientDTO(newIngredient);
    }

    @Override
    public IngredientDTO createNewIngredient(IngredientRequest ingredientRequest, UUID userId) {

        Ingredient ingredient = Ingredient.builder()
                .ingredientName(ingredientRequest.getIngredientName())
                .userId(userId)
                .ingredientIcon("074491e7-27b7-43ed-97d4-1dee238564b2.jpg")
                .ingredientType("uncategorized")
                .build();

        Ingredient newIngredient = ingredientRepository.createNewIngredient(ingredient, userId);
        return ingredientDTOConvertor.convertIngredientToIngredientDTO(newIngredient);
    }


    private UUID getCurrentUserId() {
        CustomUserDetail userDetails = (CustomUserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userDetails.getAppUser().getUserId();
    }
}
